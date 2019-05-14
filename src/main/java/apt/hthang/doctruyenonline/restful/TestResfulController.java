package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Linh
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/test_chapter")
public class TestResfulController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private StoryService storyService;
    
    @PostMapping(value = "/add_story")
    public ResponseEntity< ? > getData(@RequestParam("url") String url,
                                       @RequestParam("start") int start,
                                       @RequestParam("end") int end,
                                       @RequestParam("userId") Long userId,
                                       @RequestParam("storyId") Long storyId) throws IOException {
        Story story = storyService.findStoryByIdAndStatus(storyId, ConstantsListUtils.LIST_STORY_DISPLAY);
        User user = userService.findUserById(userId);
        for (int i = start; i <= end; i++) {
            Document doc = Jsoup.connect(url + i).timeout(5000).get();
            String[] title = doc.title().split("-");
            // Lấy Tên Chương
            String[] titleText = title[1].trim().split(" ");
            StringBuilder nameChapter = new StringBuilder();
            String chapterNumber = "";
            for (int j = 0; j < titleText.length; j++) {
                if (j == 1)
                    chapterNumber = titleText[j].replaceAll(":", " ");
                if (j > 1)
                    nameChapter.append(titleText[j]).append(" ");
            }
            // Lấy Nội Dung Chương
            Element page = doc.select("div#js-truyencv-content").first();
            String cleanString = Jsoup.parse(page.html()).wholeText().replaceAll("\n", "<br>");
            Chapter chapter = new Chapter();
            chapter.setStory(story);
            chapter.setUser(user);
            chapter.setContent(cleanString);
            chapter.setChapterNumber(chapterNumber.trim());
            chapter.setName(nameChapter.toString().trim()   );
            chapter.setSerial((float) i);
            chapterService.saveNewChapter(chapter);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
