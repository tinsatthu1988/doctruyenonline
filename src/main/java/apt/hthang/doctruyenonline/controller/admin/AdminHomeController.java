package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.ReportService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri")
public class AdminHomeController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ReportService reportService;
    
    @RequestMapping
    public String defaultAdmiHome(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("title", "Trang Quản Trị");
        model.addAttribute("newUser", userService.countUserNewInDate(java.sql.Date.valueOf(today)));
        model.addAttribute("newStory", storyService.countNewUserInDate(java.sql.Date.valueOf(today)));
        model.addAttribute("newReport", reportService.countNewReportInDay(java.sql.Date.valueOf(today)));
        model.addAttribute("newChapter", chapterService.countNewChapterInDate(java.sql.Date.valueOf(today)));
        return "/dashboard/homePage";
    }
}
