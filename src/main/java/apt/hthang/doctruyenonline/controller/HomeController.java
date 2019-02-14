package apt.hthang.doctruyenonline.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/")
    public String homePage() {
        return "homePage";
    }

    @RequestMapping(value = "/dang_nhap")
    public String loginPage() {
        return "loginPage";
    }
}
