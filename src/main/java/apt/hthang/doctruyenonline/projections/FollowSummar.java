package apt.hthang.doctruyenonline.projections;

import java.util.Date;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public interface FollowSummar {
    
    StoryFollow getStory();
    
    public interface StoryFollow {
        
        Long getId();
        
        String getVnName();
    
        Date getUpdateDate();
        
    }
}
