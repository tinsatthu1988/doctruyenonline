package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ on 04/12/2018
 * @project truyenonline
 */
public interface ChapterOfStory {

    Long getId();

    Integer getChapterNumber();

    Float getSerial();

    String getName();

    @Value("#{target.story.id}")
    Long getStoryId();

    @Value("#{@myComponent.getDisplayName(target.user.username,target.user.displayName)}")
    String getDisplayName();

    Date getCreateDate();

    @Value("#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();

    Integer getStatus();

}
