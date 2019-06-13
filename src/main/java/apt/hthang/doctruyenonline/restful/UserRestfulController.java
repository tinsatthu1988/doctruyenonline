package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Role;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.projections.TopConverter;
import apt.hthang.doctruyenonline.service.CloudinaryUploadService;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserRestfulController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private PayService payService;
    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;
    @Autowired
    private MyComponent myComponent;
    //Lấy Thông Tin Converter
    @PostMapping(value = "/converterInfo")
    public ResponseEntity< ? > loadConverter(@RequestParam("userId") Long userId) {
        ConveterSummary conveterSummary = userService.findConverterByID(userId);
        return new ResponseEntity<>(conveterSummary, HttpStatus.OK);
    }
    
    //Thay đổi ngoại hiệu
    @PostMapping(value = "/changeNick")
    @Transactional
    public ResponseEntity< ? > changeNick(@RequestParam(value = "txtChangenick") String txtChangenick,
                                          Principal principal) throws Exception {
        String decryptedText = new String(java.util.Base64.getDecoder().decode(txtChangenick));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String newNick = aesUtil.decrypt(decryptedText.split("::")[1],
                    decryptedText.split("::")[0],
                    "1234567891234567",
                    decryptedText.split("::")[2]);
            if (principal == null) {
                throw new HttpNotLoginException();
            }
            MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
            User user = myUser.getUser();
            user = userService.findUserById(user.getId());
            if (user == null) {
                throw new HttpNotLoginException("Tài khoản không tồn tại");
            }
            if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
                throw new HttpUserLockedException();
            }
            if (newNick == null || newNick.isEmpty() || newNick.length() > 25) {
                throw new HttpMyException("Ngoại hiệu không được để trống hoặc dài quá 25 ký tự!");
            }
            if (newNick.equalsIgnoreCase(user.getDisplayName())) {
                throw new HttpMyException("Ngoại hiệu này bạn đang sử dụng");
            }
            if (userService.checkUserDisplayNameExits(user.getId(), newNick)) {
                throw new HttpMyException("Ngoại hiệu đã tồn tại!");
            }
            Double money = Double.valueOf(0);
            if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                money = ConstantsUtils.PRICE_UPDATE_NICK;
            }
            if (user.getGold() < ConstantsUtils.PRICE_UPDATE_NICK) {
                throw new HttpMyException("Số dư của bạn không đủ để thanh toán!");
            }
            userService.updateDisplayName(user.getId(), money, newNick);
            payService.savePay(null, null, user, null, 0,
                    ConstantsUtils.PRICE_UPDATE_NICK, ConstantsPayTypeUtils.PAY_DISPLAY_NAME_TYPE);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new HttpMyException("Có lỗi xảy ra. Mong bạn quay lại sau");
        }
    }
    
    @PostMapping(value = "/saveNotification")
    public ResponseEntity< Object > saveUserNotification(@RequestParam(value = "notification") String notification,
                                                         Principal principal)
            throws Exception {
        String decryptedText = new String(java.util.Base64.getDecoder().decode(notification));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String newNotification = aesUtil.decrypt(decryptedText.split("::")[1],
                    decryptedText.split("::")[0],
                    "1234567891234567",
                    decryptedText.split("::")[2]);
            if (principal == null) {
                throw new HttpNotLoginException();
            }
            MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
            User user = myUser.getUser();
            user = userService.findUserById(user.getId());
            if (user == null) {
                throw new HttpNotLoginException("Tài khoản không tồn tại");
            }
            if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
                throw new HttpUserLockedException();
            }
            if (newNotification.trim().length() > 255) {
                throw new HttpMyException("Thông báo không được dài quá 255 ký tự!");
            }
            //Lấy Thông User
            user = userService.findUserById(user.getId());
            user.setNotification(newNotification.trim());
            //Cập Nhật Thông Tin User
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new HttpMyException("Có lỗi xảy ra. Mong bạn quay lại sau");
        }
    }
    
    // Cập nhật Avatar
    @PostMapping(value = "/upload")
    @Transactional
    public ResponseEntity< Object > saveUserAvatar(@RequestParam("userfile") MultipartFile uploadfile,
                                                   Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpUserLockedException();
        }
        String fileExtension = FilenameUtils.getExtension(uploadfile.getOriginalFilename());
        assert fileExtension != null;
        if (WebUtils.checkExtension(fileExtension)) {
            throw new HttpMyException("Chỉ upload ảnh có định dạng JPG | JPEG | PNG!");
        }
        if (uploadfile.getSize() > (20 * 1024 * 1024)) {
            throw new HttpMyException("Kích thước ảnh upload tối đa là 20 Megabybtes!");
        }
        String url = cloudinaryUploadService.upload(uploadfile, user.getUsername() + "-" + System.nanoTime());
        //Lấy User theo id
        user = userService.findUserById(user.getId());
        user.setAvatar(url);
        //Cập Nhật Thông Tin User
        userService.updateUser(user);
        return new ResponseEntity<>(url, HttpStatus.OK);
    }
    
    
    @PostMapping(value = "/topConveter")
    public ResponseEntity< ? > loadStoryOfConverter() {
        // Lấy Danh Sách Top Converter
        List< TopConverter > topConverters = userService
                .findTopConverter(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE);
        
        return new ResponseEntity<>(topConverters, HttpStatus.OK);
    }
    
    @PostMapping(value = "/admin/listUser")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam(value = "search", defaultValue = "") String search,
                                                    @RequestParam("type") Integer type,
                                                    @RequestParam("pagenumber") Integer pagenumer,
                                                    Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpUserLockedException();
        }
        return new ResponseEntity<>(userService.findByType(search, type, pagenumer, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity deleteUsser(@PathVariable("id") Long id, Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        User deleteUser = userService.findUserById(id);
        if (user == null) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        if (deleteUser == null)
            throw new HttpMyException("Tài khoản không tồn tại");
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpUserLockedException();
        }
        if (user.getId() == deleteUser.getId()) {
            throw new HttpMyException("Bạn không thể tự xóa tài khoản của mình");
        }
        boolean checkAdminLogin = myComponent.hasRole(user, ConstantsUtils.ROLE_ADMIN);
        boolean checkAdminUser = myComponent.hasRole(deleteUser, ConstantsUtils.ROLE_ADMIN);
        boolean checkModLogin = myComponent.hasRole(user, ConstantsUtils.ROLE_SMOD);
        boolean checkModnUser = myComponent.hasRole(deleteUser, ConstantsUtils.ROLE_SMOD);
        if ((checkAdminLogin == true && checkAdminUser == false) || (checkModLogin == true && checkModnUser == false && checkAdminUser == false)) {
            try {
                userService.deleteUser(deleteUser);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                throw new HttpMyException("Không Thể Xóa Người Dùng Này");
            }
        } else
            throw new HttpMyException("Bạn không đủ quyền xóa người dùng này");
    }
    
}
