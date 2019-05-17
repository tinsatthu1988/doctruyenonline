package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.UserFollow;
import apt.hthang.doctruyenonline.projections.FollowSummar;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
public interface UserFollowService {
    
    /**
     * @param id
     * @param pagenumber
     * @return
     */
    Page< FollowSummar > findAllStoryFollowByUserId(Long id, Integer pagenumber, Integer size);
    
    /**
     *
     * @param userId
     * @param storyId
     * @return
     */
    UserFollow findByUserIdAndStoryId(Long userId, Long storyId);
    
    /**
     *
     * @param userFollow
     */
    void deleteFollow(UserFollow userFollow);
}
