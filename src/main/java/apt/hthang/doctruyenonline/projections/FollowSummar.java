package apt.hthang.doctruyenonline.projections;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public interface FollowSummar {
    
    StoryFollow getStory();
    
    public interface StoryFollow {
        
        Long getId();
        
        String getVnName();
        
    }
}
