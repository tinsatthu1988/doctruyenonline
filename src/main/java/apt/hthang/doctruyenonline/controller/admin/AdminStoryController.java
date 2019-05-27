package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.service.CloudinaryUploadService;
import apt.hthang.doctruyenonline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri")
public class AdminStoryController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;
    
    @RequestMapping("/quan_ly_truyen")
    public String listStoryPage(Model model, Principal principal) {
        
        model.addAttribute("title", "Danh Sách Truyện");
        
        return "dashboard/adminStoryPage";
    }
    
}
