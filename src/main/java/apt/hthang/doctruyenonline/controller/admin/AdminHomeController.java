package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.ReportService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri")
public class AdminHomeController {
    
    private Logger logger = LoggerFactory.getLogger(AdminHomeController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ReportService reportService;
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
        model.addAttribute("titleMenu", "Trang Quản Trị");
    }
    
    @RequestMapping
    public String defaultAdmiHome(Model model, Principal principal) throws Exception {
        LocalDate today = LocalDate.now();
        model.addAttribute("title", "Trang Quản Trị");
        model.addAttribute("newUser", userService.countUserNewInDate(java.sql.Date.valueOf(today)));
        model.addAttribute("newStory", storyService.countNewUserInDate(java.sql.Date.valueOf(today)));
        model.addAttribute("newReport", reportService.countNewReportInDay(java.sql.Date.valueOf(today)));
        model.addAttribute("newChapter", chapterService.countNewChapterInDate(java.sql.Date.valueOf(today)));
        getUser(model, principal);
        
        return "/dashboard/homePage";
    }
}
