package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.CommentSummary;
import org.springframework.data.domain.Page;

/**
 * @author Đời Không Như Là Mơ
 */
public interface CommentService {
    
    /**
     * Lấy danh sách Comment theo truyện
     *
     * @param storyId    - id của truyện
     * @param type       - kiểu lấy danh sách
     * @param pagenumber - số trang
     * @return Page<CommentSummary>
     */
    Page< CommentSummary > getListCommentOfStory(Long storyId, int pagenumber, int type);
    
    /**
     * Lưu Comment
     *
     * @param user        - Người đăng comment
     * @param story       - Comment của Truyện
     * @param commentText - Nội dung comment
     * @return true - nếu lưu comment thành công / false - nếu có lỗi xảy ra
     */
    boolean saveComment(User user, Story story, String commentText);
}
