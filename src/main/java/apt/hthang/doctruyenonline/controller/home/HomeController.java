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
@RequestMapping(value = "/")
public class HomeController {

    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Value("${hthang.truyenmvc.title.home}")
    private String titleHome;

    //Lấy Danh sách menu thể loại và Thông Tin Web
    private void getMenuAndInfo(Model model, String title) {

        // Lấy Title Cho Page
        model.addAttribute("title", title);

        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu());

        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }

    @RequestMapping(value = "/")
    public String homePage(Model model) {
        getMenuAndInfo(model, titleHome);
        return "web/homePage";
    }

}
