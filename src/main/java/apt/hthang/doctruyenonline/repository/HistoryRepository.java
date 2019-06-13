package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 */
@Repository
public interface HistoryRepository extends JpaRepository< History, Long > {
    
    /**
     * Lấy Lịch sử đọc mới nhất
     *
     * @param userId
     * @param storyId
     * @return Optional<History>
     */
    Optional< History > findTopByUser_IdAndChapter_Story_IdAndChapter_StatusInOrderByDateViewDesc(Long userId, Long storyId, List< Integer > status);
    
    /**
     * Kiểm tra tồn tại Favorites trong khoảng
     *
     * @param chapterId
     * @param locationIP
     * @param startDate
     * @param endDate
     * @return boolean
     */
    boolean existsByChapter_IdAndLocationIPAndDateViewBetween(Long chapterId, String locationIP, Date startDate, Date endDate);
}
