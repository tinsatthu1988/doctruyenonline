package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.Mail;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.form.ForgotUser;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.EmailService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Đời Không Như Là Mơ on 15/10/2018
 * @project doctruyenonline
 */

@Controller
@PropertySource(value = {"classpath:messages.properties",
        "classpath:ValidationMessages.properties"},
        encoding = "UTF-8")
@RequestMapping("/quen-mat-khau")
public class ResetPasswordController {
    
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final EmailService emailService;
    Logger logger = LoggerFactory.getLogger(ResetPasswordController.class);
    @Value("${hthang.truyenonline.title.forgot}")
    private String titleForgot;
    @Value("${hthang.truyenonline.forgotuser.notfound.message}")
    private String notFoundUser;
    @Value("${hthang.truyenonline.email.form}")
    private String emailForm;
    @Value("${hthang.truyenonline.email.display}")
    private String emailDisplay;
    @Value("${hthang.truyenonline.email.subject}")
    private String emailSubject;
    @Value("${hthang.truyenonline.email.signature}")
    private String emailSignature;
    @Value("${hthang.truyenonline.email.url}")
    private String emailUrl;
    
    @Autowired
    public ResetPasswordController(InformationService informationService,
                                   CategoryService categoryService,
                                   UserService userService,
                                   EmailService emailService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.emailService = emailService;
    }
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @GetMapping
    public String showForgotForm(final Model model) {
        
        // Lấy List Category Menu
        getMenuAndInfo(model, titleForgot);
        
        model.addAttribute("user", new ForgotUser());
        
        return "view/forgotPage";
    }
    
    @PostMapping
    public String register(@Valid ForgotUser forgotUser, BindingResult result, Model model, RedirectAttributes redirect) {
        boolean hasError = result.hasErrors();
        if (!hasError) {
            User user = userService.findForgotUser(forgotUser.getUsername(), forgotUser.getEmail());
            if (user == null) {
                model.addAttribute("error", notFoundUser);
                hasError = true;
            } else {
                String newPassword = WebUtils.randomPassword();
                if (sendMail(user, newPassword)) {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    user.setPassword(passwordEncoder.encode(newPassword));
                    userService.updateUser(user);
                    redirect.addFlashAttribute("success", "Mật khẩu của bạn đã được thay đổi! Mời vào email để xem mật khẩu mới!");
                    return "redirect:/dang-nhap";
                } else {
                    model.addAttribute("error", "Có lỗi xảy ra mong bạn vui lòng quay lại sau!");
                    hasError = true;
                }
                
            }
        }
        if (hasError) {
            getMenuAndInfo(model, titleForgot);
            model.addAttribute("user", forgotUser);
            return "view/forgotPage";
        }
        return "redirect:/";
    }
    
    private boolean sendMail(User user, String newPassword) {
        Mail mail = new Mail();
        mail.setFrom(emailForm);
        mail.setTo(user.getEmail());
        mail.setSubject(emailSubject);
        mail.setFromDisplay(emailDisplay);
        Map< String, Object > modelMap = new HashMap< String, Object >();
        modelMap.put("name", user.getDisplayName() != null ? user.getDisplayName() : user.getUsername());
        modelMap.put("url", emailUrl);
        modelMap.put("signature", emailSignature);
        modelMap.put("password", newPassword);
        mail.setModel(modelMap);
        return emailService.sendSimpleMessage(mail, "web/mail/forgot-password");
    }
}
