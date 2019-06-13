package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ on 23/12/2018
 * @project truyenonline
 */
public interface ConveterSummary {

    Long getId();

    @Value("#{@myComponent.getDisplayName(target.username,target.displayName)}")
    String getDisplayName();

    String getAvatar();

    Double getGold();

    Date getCreateDate();

    @Value("#{@myComponent.countChapterOfUser(target.id)}")
    Long getCountChapter();

    @Value("#{@myComponent.countStoryOfUser(target.id)}")
    Long getCountStory();
}
