package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Huy Thang
 */
@Repository
public interface FavoritesRepository extends JpaRepository< Favorites, Long > {
    
    /**
     * Lấy Lịch sử đọc mới nhất
     *
     * @param userId
     * @param storyId
     * @return Optional<Favorites>
     */
    Optional< Favorites > findTopByUser_IdAndChapter_Story_IdOrderByDateViewDesc(Long userId, Long storyId);
}
