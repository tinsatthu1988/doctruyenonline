package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Huy Thang
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
    
    Integer getChapterNumber();
    
    String getDisplayName();
    
    String getUsername();
    
    Integer getDealStatus();
}
