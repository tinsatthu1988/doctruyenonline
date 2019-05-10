package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Linh
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/pay")
public class PayRestfulController {
    @Autowired
    private UserService userService;
    @Autowired
    private PayService payService;
    
    //Danh sách Giao dịch của người dùng
    @PostMapping(value = "/list_pay")
    public ResponseEntity< ? > loadListPayWithdrawOfUser(@RequestParam("pagenumber") Integer pagenumber,
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
        return new ResponseEntity<>(payService.findPageByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
    
    //Danh sách giao dịch rút tiền của người dùng
    @PostMapping(value = "/list_pay_draw")
    public ResponseEntity< ? > loadListPayOfUser(@RequestParam("pagenumber") Integer pagenumber,
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
        return new ResponseEntity<>(payService.findPagePayWithdrawByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
}
