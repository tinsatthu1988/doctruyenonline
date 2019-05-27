package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.*;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.EmailService;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuyThang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/pay")
public class PayRestfulController {
    
    Logger logger = LoggerFactory.getLogger(PayRestfulController.class);
    
    @Autowired
    private UserService userService;
    @Autowired
    private PayService payService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private MyComponent myComponent;
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
        try {
            Long result = payService.savePayDraw(user, money);
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
        } catch (Exception e) {
            throw new HttpMyException("Có lỗi xảy ra! Hãy Thực hiện lai sau!");
        }
    }
    
    //THực Hiện Nạp Tiền Cho Người Dùng
    @PostMapping(value = "/rechange")
    public ResponseEntity< ? > submitPayDraw(@RequestParam("money") String money,
                                             @RequestParam("reId") Long reId, Principal principal) throws Exception {
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
        if (!checkRole(user, ConstantsUtils.ROLE_ADMIN))
            throw new HttpMyException("Bạn không có quyền thực hiện hành động này!");
        User user1Re = userService.findUserById(reId);
        if (user1Re == null)
            throw new HttpMyException("Người Nhận không tồn tại!");
        if (WebUtils.checkMoney(money))
            throw new HttpMyException("Số đậu nạp phải là số và lớn hơn 0!");
        try {
            payService.savePayChange(user, Double.valueOf(money), user1Re);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new HttpMyException("Có lỗi xảy ra! Hãy Thực hiện lai sau!");
        }
    }
    
    private boolean checkRole(User use, Integer id) {
        for (Role role : use.getRoleList()) {
            if (role.getId() == id)
                return true;
        }
        return false;
    }
    
    // Thực Hiện Giao Dịch Thanh Toán Đọc Chapter Vip
    @PostMapping(value = "/readingVip")
    @Transactional
    public ResponseEntity< ? > payReadingChapter(@RequestParam(value = "chapterId") String chapterId, Principal principal)
            throws Exception {
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
        if (chapterId == null || WebUtils.checkLongNumber(chapterId)) {
            throw new HttpMyException("Có lỗi xảy ra! Mong bạn quay lại sau.");
        }
        Chapter chapter = chapterService
                .findChapterByIdAndStatus(Long.valueOf(chapterId), ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        if (chapter == null) {
            throw new HttpMyException("Không tồn tại chương truyện này!");
        }
        if (chapter.getStatus() == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();
        
        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);
        boolean check = payService.checkDealChapterVip(Long.valueOf(chapterId), user.getId(), dayAgo, now);
        logger.info("Id chapter: " + chapterId);
        logger.info("Thời gian kiểm tra: " + dayAgo + " - " + now);
        logger.info("Kiểm Tra: " + check);
        if (check) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (user.getGold() < chapter.getPrice()) {
            throw new HttpMyException("Số dư trong tài khoản không đủ để thanh toán!");
        }
        try {
            payService.saveReadingVipPay(user, chapter);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            throw new HttpMyException("Có lỗi xảy ra. Vui lòng thử lại sau");
        }
    }
}
