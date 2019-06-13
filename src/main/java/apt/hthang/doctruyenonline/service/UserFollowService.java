package apt.hthang.doctruyenonline.service;

import org.springframework.data.domain.Page;

import apt.hthang.doctruyenonline.entity.UserFollow;
import apt.hthang.doctruyenonline.projections.FollowSummar;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
public interface UserFollowService {
    
    /**
     * @param id
     * @param pagenumber
     * @return Page< FollowSummar >
     */
    Page< FollowSummar > findAllStoryFollowByUserId(Long id, Integer pagenumber, Integer size);
    
    /**
     * @param userId
     * @param storyId
     * @return UserFollow
     */
    UserFollow findByUserIdAndStoryId(Long userId, Long storyId);
    
    /**
     * @param userId - Id của người dùng
     * @param storyId- Id của Truyện
     * @return {@code true} nếu tồn tại, nếu không tồn tại thì {@code false}
     */
    boolean existsUserFollow(Long userId, Long storyId);

    /**
     * @param userFollow
     */
    void deleteFollow(UserFollow userFollow);
    
    /**
     *
     * @param userFollow
     */
    void saveFollow(UserFollow userFollow);
}
