package apt.hthang.doctruyenonline.projections;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
public interface UserAdmin {
    
    Long getId();
    
    String getUsername();
    
    String getDisplayName();
    
    Date getCreateDate();
    
    Integer getStatus();
}
