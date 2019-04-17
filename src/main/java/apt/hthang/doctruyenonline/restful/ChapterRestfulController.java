package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.projections.ChapterOfStory;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/chapter")
public class ChapterRestfulController {
    
    private final ChapterService chapterService;
    
    public ChapterRestfulController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }
    
    @PostMapping(value = "/chapterOfStory")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("type") Integer type) {
        Page< ChapterOfStory > chapterOfStoryPage = chapterService
                .getListChapterOfStory(storyId, pagenumber, ConstantsListUtils.LIST_CHAPTER_DISPLAY, type);
        return new ResponseEntity<>(chapterOfStoryPage, HttpStatus.OK);
    }
    
}
