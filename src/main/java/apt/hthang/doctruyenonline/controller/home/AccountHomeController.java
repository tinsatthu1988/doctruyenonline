package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
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

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/tai-khoan")
public class AccountHomeController {
    
    private final static Logger logger = LoggerFactory.getLogger(AccountHomeController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private ChapterService chapterService;
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @RequestMapping
    private String defaultHomePage(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        String title = user.getDisplayName() != null ? user.getDisplayName() : user.getUsername();
        if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
            user.setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }
        
        model.addAttribute("user", user);
        
        getMenuAndInfo(model, title);
        
        loadStory_ChapterByUser(user.getId(), model);
        return "web/view/accHomePage";
    }
    
    
    // Lấy Số Chapter Và Số Truyện Đăng bởi User
    private void loadStory_ChapterByUser(Long userId, Model model) {
        model.addAttribute("countStory", storyService.
                countStoryByUser(userId, ConstantsListUtils.LIST_STORY_DISPLAY));
        model.addAttribute("countChapter", chapterService
                .countChapterByUser(userId, ConstantsListUtils.LIST_CHAPTER_DISPLAY));
    }
}
