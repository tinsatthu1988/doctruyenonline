package apt.hthang.doctruyenonline.controller.account;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.form.ChangePassword;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan/")
public class AccountStoryController {
    
    private final static Logger logger = LoggerFactory.getLogger(AccountStoryController.class);
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;
    @Autowired
    private StoryService storyService;
    
    private void getMenuAndInfo(Model model, String title, Integer typePage) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
        
        model.addAttribute("typePage", typePage);
    }
    
    @RequestMapping("/quan_ly_truyen")
    public String listStoryPage(Model model, Principal principal) throws Exception {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }
        
        getMenuAndInfo(model, "Danh sách truyện đã đăng", 6);
        
        model.addAttribute("id", user.getId());
        return "view/account/accStoryPage";
    }
    
    @GetMapping("/them_truyen")
    public String addStoryPage(Model model) {
        
        getMenuAndInfo(model, "Đăng Truyện", 5);
    
        if (!model.containsAttribute("story")) {
            model.addAttribute("story", new Story());
        }
        
        return "view/account/addStoryPage";
    }
    
    @PostMapping("/them_truyen")
    public String saveStoryPage(@Valid Story story, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) {
        boolean hasError = result.hasErrors();
        if (hasError) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.story", result);
            redirectAttributes.addFlashAttribute("story", story);
            return "redirect:/tai-khoan/them_truyen";
        }
        //Lấy Thông Tin người dùng đăng nhập
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        
        story.setUser(loginedUser.getUser());
        String url = cloudinaryUploadService
                .upload(story.getUploadfile(), loginedUser.getUser().getUsername() + "-" + System.nanoTime());
        story.setImages(url);
        boolean check = storyService.newStory(story);
        if (check) {
            redirectAttributes.addFlashAttribute("checkAddStoryTrue", "Đăng truyện mới thành công!");
        } else {
            redirectAttributes.addFlashAttribute("checkAddStoryFalse", "Có lỗi xảy ra, Đăng truyện thất bại!");
        }
        return "redirect:/tai-khoan/quan_ly_truyen";
    }
    
    @GetMapping("/sua_truyen/{id}")
    public String addStoryPage(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttrs,
                               Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        Story story = storyService.findStoryById(id);
        if (story == null) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Truyện không tồn tại");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (!story.getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Bạn không có quyền sửa truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        
        getMenuAndInfo(model, "Sửa Truyện", -1);
        
        model.addAttribute("story", story);
        
        model.addAttribute("statusList", ConstantsListUtils.LIST_STORY_STATUS_CONVERTER);
        return "web/account/editStoryPage";
    }
    
    @PostMapping("/sua_truyen/save")
    public String saveStoryEditPage(@Valid Story storyEdit, BindingResult result, Model model, Principal principal, RedirectAttributes redirectAttrs) throws NotFoundException {
        boolean hasError = result.hasErrors();
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        if (!storyEdit.getUser().getId().equals(loginedUser.getUser().getId())) {
            redirectAttrs.addFlashAttribute("checkEditStory", "Bạn không có quyền sửa truyện không do bạn đăng!");
            return "redirect:/tai-khoan/quan_ly_truyen";
        }
        if (hasError) {
            getMenuAndInfo(model, "Sửa Truyện", -1);
            model.addAttribute("statusList", ConstantsListUtils.LIST_STORY_STATUS_CONVERTER);
            model.addAttribute("story", storyEdit);
            return "web/account/editStoryPage";
        }
        
        if (!storyEdit.getEditfile().isEmpty() && storyEdit.getEditfile() != null) {
            String url = cloudinaryUploadService
                    .upload(storyEdit.getEditfile(), loginedUser.getUser().getUsername() + "-" + System.nanoTime());
            storyEdit.setImages(url);
        }
        boolean check = storyService.updateStory(storyEdit);
        redirectAttrs.addFlashAttribute("checkEditStory", check);
        return "redirect:/tai-khoan/quan_ly_truyen";
    }
}
