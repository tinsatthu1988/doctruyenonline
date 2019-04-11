package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Huy Thang on 20/11/2018
 * @project truyenonline
 */
public interface ChapterSummary {

    Long getId();

    String getChapterNumber();

    Float getSerial();

    String getName();

    @Value("#{target.story.id}")
    Long getStoryId();

    Integer getStatus();

    @Value("#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();
}
