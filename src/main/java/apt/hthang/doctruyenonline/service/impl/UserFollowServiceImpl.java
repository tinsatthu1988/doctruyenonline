package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.UserFollow;
import apt.hthang.doctruyenonline.projections.FollowSummar;
import apt.hthang.doctruyenonline.repository.UserFollowRepository;
import apt.hthang.doctruyenonline.service.UserFollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Service
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowRepository userFollowRepository;
    
    /**
     * @param id
     * @param pagenumber
     * @return
     */
    @Override
    public Page< FollowSummar > findAllStoryFollowByUserId(Long id, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return userFollowRepository.findByUser_IdOrderByStory_UpdateDateDesc(id, pageable);
    }
    
    /**
     * @param userId
     * @param storyId
     * @return
     */
    @Override
    public UserFollow findByUserIdAndStoryId(Long userId, Long storyId) {
        return userFollowRepository.findByUser_IdAndStory_Id(userId, storyId).orElse(null);
    }
    

    
    /**
     * @param userFollow
     */
    @Override
    public void deleteFollow(UserFollow userFollow) {
        userFollowRepository.delete(userFollow);
    }
    
    @Override
    public void saveFollow(UserFollow userFollow) {
        userFollowRepository.save(userFollow);
    }

     /**
     * @param userId - Id của người dùng
     * @param storyId- Id của Truyện
     * @return {@code true} nếu tồn tại, nếu không tồn tại thì {@code false}
     */
    @Override
    public boolean existsUserFollow(Long userId, Long storyId) {
        return userFollowRepository.existsByUser_IdAndStory_Id(userId,storyId);
    }
}
