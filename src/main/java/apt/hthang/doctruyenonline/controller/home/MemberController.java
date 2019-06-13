package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Đời Không Như Là Mơ on 27/11/2018
 * @project truyenonline
 */

@Controller
@RequestMapping("/member")
public class MemberController {
    
    Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private InformationService informationService;
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @RequestMapping("/{userId}")
    public String defaultMemberController(@PathVariable("userId") String userId, Model model) throws NotFoundException {
        if (userId == null || WebUtils.checkLongNumber(userId)) {
            throw new NotFoundException();
        }
        Long uID = Long.valueOf(userId);
        
        User user = checkMember(uID);
        
        String title = user.getDisplayName() != null ? user.getDisplayName() : user.getUsername();
        
        model.addAttribute("user", user);
        
        loadStory_ChapterByUser(uID, model);
        
        getMenuAndInfo(model, title);
        
        return "view/memberPage";
    }
    
    //Lay thong tin User theo uID
    private User checkMember(Long userId) throws NotFoundException {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Không Tồn Tại User Có ID :" + userId);
        }
        return user;
    }
    
    // Lấy Số Chapter Và Số Truyện Đăng bởi User
    private void loadStory_ChapterByUser(Long userId, Model model) {
        model.addAttribute("countStory", storyService.
                countStoryByUser(userId, ConstantsListUtils.LIST_STORY_DISPLAY));
        model.addAttribute("countChapter", chapterService
                .countChapterByUser(userId, ConstantsListUtils.LIST_CHAPTER_DISPLAY));
    }
    
}
