package apt.hthang.doctruyenonline.service;

/**
 * @author Huy Thang
 */
public interface UserRatingService {
    /**
     * Kiểm tra Tồn Tại UserRating theo
     *
     * @param storyId
     * @param userId
     * @return "true" nếu đã có bình chọn, "false" nếu chưa có bình chọn phù hợp
     */
    boolean existsRatingWithUser(Long storyId, Long userId);
    
    /**
     * Đếm số lượng đánh giá của Truyện
     *
     * @param storyId
     * @return Long
     */
    Long countRatingStory(Long storyId);
}
