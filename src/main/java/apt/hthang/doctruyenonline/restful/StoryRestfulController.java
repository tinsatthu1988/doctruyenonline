package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.projections.StoryMember;
import apt.hthang.doctruyenonline.projections.StorySlide;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/story")
public class StoryRestfulController {
    
    private final StoryService storyService;
    
    @Autowired
    public StoryRestfulController(StoryService storyService) {
        this.storyService = storyService;
    }
    
    //Lấy Top 3 Truyện mới đăng của Converter
    @PostMapping(value = "/storyOfConverter")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam("userId") Long userId) {
        List< StorySlide > list = storyService
                .findStoryOfConverter(userId, ConstantsListUtils.LIST_STORY_DISPLAY);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    @PostMapping(value = "/storyOfMember")
    public ResponseEntity< ? > loadStoryOfMember(@RequestParam("userId") Long userId,
                                                 @RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("type") int type) {
        Page< StoryMember > storyMembers = storyService
                .findStoryByUserId(userId, ConstantsListUtils.LIST_STORY_DISPLAY,
                        pagenumber, type, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(storyMembers, HttpStatus.OK);
    }
}
