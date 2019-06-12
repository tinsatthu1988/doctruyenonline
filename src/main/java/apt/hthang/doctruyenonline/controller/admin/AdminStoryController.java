package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.CloudinaryUploadService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri/truyen")
public class AdminStoryController {
    
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;
    @Autowired
    private MyComponent myComponent;
    
    //Lấy Thông Tin Tên User Login, Avatar User Login
    private void getUser(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        model.addAttribute("checkRole", myComponent.hasRole(user, ConstantsUtils.ROLE_ADMIN));
        model.addAttribute("avatar", myComponent.checkAvatar(user.getAvatar()));
        model.addAttribute("displayname", myComponent.getDisplayName(user.getUsername(), user.getDisplayName()));
        model.addAttribute("titleMenu", "Quản Lý Truyện");
    }
    
    @RequestMapping
    public String listStoryPage(Model model, Principal principal) throws Exception {
        
        model.addAttribute("title", "Danh Sách Truyện");
        
        getUser(model, principal);
        
        return "dashboard/adminStoryPage";
    }
    
    @GetMapping("/cap_nhat/{id}")
    public String updateStory(Model model, Principal principal, @PathVariable("id") Long id, RedirectAttributes redirectAttrs) throws Exception {

        model.addAttribute("title", "Cập Nhật Truyện");
        
        getUser(model, principal);

        Story story = storyService.findStoryById(id);
        if(story == null){
            redirectAttrs.addFlashAttribute("checkEditStoryFalse", "Truyện không tồn tại!");
            return "redirect:/quan-tri/truyen";
        }
    
        if (!model.containsAttribute("story")) {
            story.setInfomation(story.getInfomation().replaceAll("(?i)<br */?>", "\n"));
            model.addAttribute("story", story);
        }

        model.addAttribute("statusVipList", ConstantsListUtils.LIST_STORY_STATUS_VIP);

        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        model.addAttribute("statusList", ConstantsListUtils.LIST_STORY_STATUS_CONVERTER);
        
        return "dashboard/editStoryPage";
    }

}
