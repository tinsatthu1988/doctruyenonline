package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.service.SecurityService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Đời Không Như Là Mơ on 12/10/2018
 * @project truyenmvc
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping("/dang-ky")
public class RegisterController {
    
    private final InformationService informationService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final SecurityService securityService;
    Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Value("${hthang.truyenonline.title.register}")
    private String titleRegisterPage;
    
    @Autowired
    public RegisterController(InformationService informationService, CategoryService categoryService, UserService userService, SecurityService securityService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.securityService = securityService;
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
    public String showRegisterForm(final Model model) {
        logger.info("Get Dang ky");
        // Lấy List Category Menu
        getMenuAndInfo(model, titleRegisterPage);
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "view/registerPage";
    }
    
    @PostMapping
    public String register(@Valid User user, BindingResult result, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/dang-ky";
        }
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        user.setPassword(passwordEncoder.encode(user.getPasswordRegister()));
        
        //Lưu Người dùng đăng ký trong database
        userService.registerUser(user);
        
        //Đăng nhập sau khi đăng ký thành công
        securityService.autologin(user.getUsername(), user.getPasswordRegisterConfirm(), request);
        
        return "redirect:/";
    }
}
