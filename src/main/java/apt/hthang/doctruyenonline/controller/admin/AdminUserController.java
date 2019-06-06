package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri/nguoi_dung")
public class AdminUserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private MyComponent myComponent;
    
    //Lấy Thông Tin Tên User Login, Avatar User Login
    private void getUser(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        model.addAttribute("avatar", myComponent.checkAvatar(user.getAvatar()));
        model.addAttribute("displayname", myComponent.getDisplayName(user.getUsername(), user.getDisplayName()));
        model.addAttribute("titleMenu", "Quản Lý Người Dùng");
    }
    
    @RequestMapping
    public String defaultAdminHome(Model model, Principal principal) throws Exception {
        
        model.addAttribute("title", "Danh Sách Người Dùng");
        
        getUser(model, principal);
        
        return "/dashboard/adminUserPage";
    }
    
    @RequestMapping("/cap_nhat/{id}")
    public String updateUser(Model model, Principal principal, @PathVariable("id") Long id, RedirectAttributes redirectAttrs) throws Exception {
        
        model.addAttribute("title", "Cập Nhật Người Dùng");
        
        getUser(model, principal);
        
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        User editUser = userService.findUserById(id);
        return "/dashboard/editUserPage";
    }
}
