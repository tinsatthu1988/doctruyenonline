package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.Mail;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Pay;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.EmailService;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuyThang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/pay")
public class PayRestfulController {
    @Autowired
    private UserService userService;
    @Autowired
    private PayService payService;
    @Autowired
    private EmailService emailService;
    @Value("${hthang.truyenonline.email.form}")
    private String emailForm;
    @Value("${hthang.truyenonline.email.display}")
    private String emailDisplay;
    @Value("${hthang.truyenonline.email.signature}")
    private String emailSignature;
    @Value("${hthang.truyenonline.email.url}")
    private String emailUrl;
    
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
    
    //Danh sách giao dịch rút tiền của người dùng
    @DeleteMapping(value = "/cancel_pay/{payId}")
    public ResponseEntity< ? > deletePayDraw(@PathVariable("payId") Long payId,
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
        
        //Lấy Thông Tin Giao Dịch
        Pay pay = payService.findPayById(payId);
        if (pay == null) {
            throw new HttpMyException("Không Tồn Tại Giao Dịch có mã GD: " + payId);
        }
        if (!pay.getUserSend().getId().equals(user.getId()))
            throw new HttpNotLoginException("Bạn không có quyền hủy Giao dịch không phải của bản thân!");
        if (pay.getStatus() != 2) {
            throw new HttpMyException("Giao dịch với mã: " + payId + " không thể hủy!");
        }
        boolean result = payService.cancelWithDraw(pay.getId());
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new HttpMyException("Có lỗi xảy ra! Hãy Thực hiện lai sau!");
    }
    
    //THực Hiện Đăng Ký Rút Tiền
    @PostMapping(value = "/save_draw_pay")
    public ResponseEntity< ? > submitPayDraw(@RequestParam("money") Double money, @RequestParam("moneyVND") Double vnd,
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
        if (vnd % 10000 != 0 && vnd < 50000)
            throw new HttpMyException("Số Tiền Cần đổi phải chia hết cho 10000! Rút ít nhất 50000VND");
        if (user.getGold() < money)
            throw new HttpMyException(" Số dư tài khoản bạn không đủ!");
        Long result = payService.newPayWithDraw(user.getId(), money);
        if (result != 0) {
            Mail mail = new Mail();
            mail.setFrom(emailForm);
            mail.setTo(user.getEmail());
            mail.setSubject("Thông báo đăng ký rút tiền!");
            mail.setFromDisplay(emailDisplay);
            Map< String, Object > modelMap = new HashMap< String, Object >();
            modelMap.put("name", user.getDisplayName() != null ? user.getDisplayName() : user.getUsername());
            modelMap.put("url", emailUrl);
            modelMap.put("signature", emailSignature);
            modelMap.put("pay", payService.findPayById(result));
            mail.setModel(modelMap);
            emailService.sendSimpleMessage(mail, "web/mail/withdraw-money");
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            throw new HttpMyException("Có lỗi xảy ra! Hãy Thực hiện lai sau!");
    }
}
