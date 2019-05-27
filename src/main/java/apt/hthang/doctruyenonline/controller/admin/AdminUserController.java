package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.ReportService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri")
public class AdminUserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ReportService reportService;
    
    @RequestMapping("/list_user")
    public String defaultAdmiHome(Model model) {
        model.addAttribute("title", "Danh Sách Người Dùng");
        return "/dashboard/adminUserPage";
    }
    
    
}
