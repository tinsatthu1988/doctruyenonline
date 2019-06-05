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
    public String defaultAdmiHome(Model model, Principal principal) throws Exception {
        
        model.addAttribute("title", "Danh Sách Người Dùng");
        
        getUser(model, principal);
        
        return "/dashboard/adminUserPage";
    }
    
    @RequestMapping("/thong_tin/{id}")
    public String informationUser(Model model, Principal principal, @PathVariable("id") Long id, RedirectAttributes redirectAttrs){
        User user = userService.findUserById(id);
        if (user == null) {
            redirectAttrs.addFlashAttribute("checkUserInformation", "Không Tồn Tại Người Dùng có Id " + id);
            return "redirect:/quan-tri/nguoi_dung";
        }
        //Lấy Thông Tin Số truyện đang Đăng Bởi User
        Long storyGoing = storyService.countStoryByUserWithStatus(user.getId(), ConstantsStatusUtils.STORY_STATUS_GOING_ON);
        model.addAttribute("user", user);

        return "/dashboard/informationUserPage";
    }
    
}
