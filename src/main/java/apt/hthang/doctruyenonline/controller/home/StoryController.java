package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.StorySummary;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Objects;

/**
 * @author Đời Không Như Là Mơ on 17/10/2018
 * @project truyenonline
 */

@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping("/truyen")
public class StoryController {
    
    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StoryService storyService;
    @Autowired
    private UserRatingService userRatingService;
    @Autowired
    private HistoryService historyService;
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @RequestMapping("/{storyId}")
    public String defaultStoryController(@PathVariable("storyId") String storyId,
                                         Principal principal,
                                         Model model) throws Exception {
        
        StorySummary story = checkStoryID(storyId);
        
        model.addAttribute("story", story);
        
        User user = getUserLogin(principal);
        
        getRating(model, user, story);
        
        getMenuAndInfo(model, story.getVnName());
        
        checkConverter(model, user, story);
        
        getChapterReadByUser(user, story.getId(), model);
        
        return "view/storyPage";
    }
    
    private StorySummary checkStoryID(String storyId) throws Exception {
        
        // Kiểm tra storyId != null
        // Kiểm tra storyId có phải kiểu long
        if (storyId == null || WebUtils.checkLongNumber(storyId)) {
            throw new NotFoundException();
        }
        
        return storyService.findStoryByStoryIdAndStatus(Long.parseLong(storyId),
                ConstantsListUtils.LIST_STORY_DISPLAY);
    }
    
    // Lấy Thông Tìn Người dùng
    // return User - nếu người dùng đã đăng nhập thành công
    // return null - nếu người dùng chưa đăng nhập
    private User getUserLogin(Principal principal) {
        User user = null;
        if (principal != null) {
            
            // Lấy Người Dùng đang đăng nhập
            MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
            user = loginedUser.getUser();
        }
        return user;
    }
    
    private void getRating(Model model,
                           User user,
                           StorySummary story) {
        // Chưa đăng nhập
        boolean checkRating = false;
        if (user != null) {
            //Nếu người đọc là người đăng thì tính là đã đánh giá
            if (story.getUserId().equals(user.getId())) {
                // Người đọc là Converter
                checkRating = true;
            } else {
                
                // Kiểm tra Người dùng đã đánh giá chưa
                if (userRatingService.existsRatingWithUser(story.getId(), user.getId())) {
                    // Người dùng đã đánh giá
                    checkRating = true;
                }
            }
        }
        model.addAttribute("countRating", userRatingService.countRatingStory(story.getId()));
        model.addAttribute("rating", checkRating);
    }
    
    // Kiểm Tra Người Dùng có phải Converter của truyện không
    // false - Nếu Chưa Login hoặc không phải Converter
    // true - Nếu là Converter của truyện
    private void checkConverter(Model model,
                                User user,
                                StorySummary story) {
        
        boolean checkConverter = false;
        if (user != null) {
            checkConverter = Objects.equals(user.getId(), story.getUserId());
        }
        model.addAttribute("checkConverter", checkConverter);
    }
    
    // Tìm Kiếm Thông Tin Chapter mới đọc của Người Dùng
    // null - Nếu người dùng chưa đăng nhập hoặc chưa đọc
    // chapter - Nếu tìm thấy dữ liệu phù hợp
    private void getChapterReadByUser(User user, Long sID, Model model) {
        Chapter chapter = null;
        if (user != null) {
            chapter = historyService.findChapterReadByUser(user.getId(), sID);
        }
        model.addAttribute("readChapter", chapter);
    }
}
