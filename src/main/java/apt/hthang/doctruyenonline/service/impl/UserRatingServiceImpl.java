package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.repository.UserRatingRepository;
import apt.hthang.doctruyenonline.service.UserRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Thang
 */
@Service
public class UserRatingServiceImpl implements UserRatingService {
    
    private final UserRatingRepository userRatingRepository;
    
    @Autowired
    public UserRatingServiceImpl(UserRatingRepository userRatingRepository) {
        this.userRatingRepository = userRatingRepository;
    }
    
    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param storyId
     * @param userId
     * @return "true" nếu đã có bình chọn, "false" nếu chưa có bình chọn phù hợp
     */
    @Override
    public boolean existsRatingWithUser(Long storyId, Long userId) {
        return userRatingRepository.existsSratingByStory_IdAndUser_Id(storyId, userId);
    }
    
    /**
     * Đếm số lượng đánh giá của Truyện
     *
     * @param storyId
     * @return Long
     */
    @Override
    public Long countRatingStory(Long storyId) {
        return userRatingRepository
                .countByStory_Id(storyId);
    }
}
