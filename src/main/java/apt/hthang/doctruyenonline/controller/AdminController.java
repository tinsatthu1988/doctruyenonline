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
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping(value = "/quan_tri/")
    public String homePage() {
        return "homeAdminPage";
    }

    @RequestMapping(value = "/quan_tri/dang_nhap")
    public String loginPage() {
        return "loginAdminPage";
    }
}
