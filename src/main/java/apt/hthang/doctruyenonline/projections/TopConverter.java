package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Huy Thang
 */
public interface TopConverter {
    
    Long getId();
    
    @Value("#{@myComponent.getDisplayName(target.username,target.displayName)}")
    String getDisplayName();
    
    String getAvatar();
    
    Long getCnt();
    
    Long getScnt();

}
