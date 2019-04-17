package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
public class HomeController {
    
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
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
        
        getMenuAndInfo(model, titleHomePage);
        return "web/view/homePage";
    }
    
    @RequestMapping(value = "/dang-nhap")
    public String loginPage(Model model) {
        if (principal != null) {
            return "redirect:/";
        }
        getMenuAndInfo(model, titleLoginPage);
        return "web/view/loginPage";
    }
    
}
