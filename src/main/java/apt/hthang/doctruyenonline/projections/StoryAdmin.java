package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Đời Không Như Là Mơ
 * @project truyenonline
 */
public interface StoryAdmin {
    
    Long getId();
    
    String getVnName();
    
    String getImages();
    
    String getAuthor();
    
    Float getRating();

    Integer getCountView();

    Integer getDealStatus();
    
    @Value("#{@myComponent.getDisplayName(target.user.username, target.user.displayName)}")
    String getDisplayName();
    
    Integer getStatus();
}
