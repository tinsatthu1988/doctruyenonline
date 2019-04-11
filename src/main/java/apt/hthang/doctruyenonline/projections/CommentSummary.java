package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public interface CommentSummary {
    
    Long getId();
    
    String getContent();
    
    @Value(value = "#{@myComponent.getBetewwen(target.createDate)}")
    String getTimeUpdate();
    
    UserSummary getUser();
}
