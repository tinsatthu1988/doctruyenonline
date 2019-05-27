package apt.hthang.doctruyenonline.projections;

import java.util.Date;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public interface UserAdmin {
    
    Long getId();
    
    String getUsername();
    
    String getDisplayName();
    
    Date getCreateDate();
    
    Integer getStatus();
}
