package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.UserRating;
import apt.hthang.doctruyenonline.entity.UserRatingPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface UserRatingRepository extends JpaRepository< UserRating, UserRatingPK > {
    
    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param storyId
     * @param userId
     * @return "true" nếu đã có bình chọn/ "false" nếu chưa có bình chọn phù hợp
     */
    boolean existsSratingByStory_IdAndUser_Id(Long storyId, Long userId);
    
    /**
     * Đếm số đánh giá
     *
     * @param storyId
     * @return Long
     */
    Long countByStory_Id(Long storyId);
}
