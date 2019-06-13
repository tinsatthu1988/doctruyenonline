package apt.hthang.doctruyenonline.controller.account;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.form.ChangePassword;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan")
public class AccountHomeController {
    
    private final static Logger logger = LoggerFactory.getLogger(AccountHomeController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    
    private void getMenuAndInfo(Model model, String title, Integer typePage) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
        
        model.addAttribute("typePage", typePage);
    }
    
    @RequestMapping
    private String defaultHomePage(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        String title = user.getDisplayName() != null ? user.getDisplayName() : user.getUsername();
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }
        
        model.addAttribute("user", userService.findInfoUserById(user.getId()));
        
        getMenuAndInfo(model, title, 0);
        
        loadStory_ChapterByUser(user.getId(), model);
        return "view/account/accHomePage";
    }
    
    
    // Lấy Số Chapter Và Số Truyện Đăng bởi User
    private void loadStory_ChapterByUser(Long userId, Model model) {
        model.addAttribute("countStory", storyService.
                countStoryByUser(userId, ConstantsListUtils.LIST_STORY_DISPLAY));
        model.addAttribute("countChapter", chapterService
                .countChapterByUser(userId, ConstantsListUtils.LIST_CHAPTER_DISPLAY));
    }
    
    @GetMapping(value = "/doi_mat_khau")
    private String changePasswordPage(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        String title = "Đổi Mật Khẩu";
        if (!model.containsAttribute("changePassword")) {
            model.addAttribute("changePassword", new ChangePassword());
        }
        
        getMenuAndInfo(model, title, 1);
        
        
        return "view/account/accPasswordPage";
    }
    
    @PostMapping(value = "/doi_mat_khau")
    private String saveNewPassword(@Valid ChangePassword changePassword, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttributes)
            throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        String title = "Đổi Mật Khẩu";
        
        getMenuAndInfo(model, title, 1);
        
        if (changePassword.getOldPassword() != null && !WebUtils.equalsPassword(changePassword.getOldPassword(), user.getPassword())) {
            result.addError(new FieldError("changePassword", "oldPassword", "Mật Khẩu Cũ không chính xác"));
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePassword", result);
            redirectAttributes.addFlashAttribute("changePassword", changePassword);
            return "redirect:/tai-khoan/doi_mat_khau";
        }
        //Lấy User theo Id
        user = userService.findUserById(user.getId());
        user.setPassword(WebUtils.encrypString(changePassword.getNewPassword()));
        //Cập Nhật User
        userService.updateUser(user);
        model.addAttribute("success", true);
        return "view/account/accPasswordPage";
    }
    
    @GetMapping(value = "/theo_doi")
    private String followPage(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        String title = "Danh Sách Theo Dõi";
        
        getMenuAndInfo(model, title, 7);
        model.addAttribute("id", user.getId());
        
        return "view/account/accFollowPage";
    }
}
