package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
public interface StoryUpdate {
    
    Long getId();
    
    String getVnName();
    
    String getImages();
    
    String getAuthor();
    
    @Value("#{@myComponent.getBetewwen(target.updateDate)}")
    String getTimeUpdate();
    
    Long getChapterId();
    
    String getInfomation();
    
    Integer getChapterNumber();
    
    @Value("#{@myComponent.getDisplayName(target.username, target.displayName)}")
    String getDisplayName();
    
    Integer getDealStatus();
}
