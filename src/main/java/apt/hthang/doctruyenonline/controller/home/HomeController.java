package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.projections.StoryTop;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import apt.hthang.doctruyenonline.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class HomeController {
    private final static Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StoryService storyService;
    @Value("${hthang.truyenonline.title.home}")
    private String titleHomePage;
    @Value("${hthang.truyenonline.title.login}")
    private String titleLoginPage;
    
    //Lấy Danh sách menu thể loại và Thông Tin Web
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(1));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @RequestMapping(value = "/")
    public String homePage(Model model) {
        //Lấy ngày bắt đầu của tuần
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek();
        
        //Lấy ngày kết thúc của tuần
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek();
        //Lấy Top View Truyện Vip Trong Tháng
        List< StoryTop > topStoryWeek = storyService.
                findStoryTopViewByStatuss(ConstantsListUtils.LIST_STORY_DISPLAY, firstDayOfWeek, lastDayOfWeek,
                        ConstantsStatusUtils.HISTORY_VIEW, ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_TOP_VIEW_DEFAULT)
                .get()
                .collect(Collectors.toList());
        model.addAttribute("topStoryWeek", topStoryWeek);
        
        // Lấy Danh Sách Truyện Vip Mới Cập Nhật
        List< StoryUpdate > topvipstory = storyService.findStoryVipUpdateByStatus(ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY, ConstantsStatusUtils.CATEGORY_ACTIVED,
                ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_DEFAULT)
                .get()
                .collect(Collectors.toList());
        model.addAttribute("vipStory", topvipstory);
        getMenuAndInfo(model, titleHomePage);
        return "view/homePage";
    }
    
    @RequestMapping(value = "/dang-nhap")
    public String loginPage(Model model, Principal principal) {
        if (principal != null) {
            return "redirect:/";
        }
        getMenuAndInfo(model, titleLoginPage);
        return "view/loginPage";
    }
    
    @RequestMapping(value = "/dang-xuat")
    public String logoutPage(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        if (httpSession != null)
            httpSession.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/dang-nhap";
    }
}
