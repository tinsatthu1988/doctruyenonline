package apt.hthang.doctruyenonline.component;

import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Huy Thang on 22/11/2018
 * @project truyenonline
 */

@Component
public class ScheduledTasks {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    
    private final ChapterService chapterService;
    private final StoryService storyService;
    
    @Autowired
    public ScheduledTasks(ChapterService chapterService, StoryService storyService) {
        this.chapterService = chapterService;
        this.storyService = storyService;
    }
    
    @Scheduled(fixedRate = 300000)
    public void updateStatusVipChapter() {
        chapterService.updateStatusChapterVip();
    }

}
