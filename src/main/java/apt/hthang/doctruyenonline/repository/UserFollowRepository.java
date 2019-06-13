package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.UserFollow;
import apt.hthang.doctruyenonline.entity.UserFollowPK;
import apt.hthang.doctruyenonline.projections.FollowSummar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Repository
public interface UserFollowRepository extends JpaRepository< UserFollow, UserFollowPK > {
    
    /**
     * @param id
     * @return
     */
    Page< FollowSummar > findByUser_IdOrderByStory_UpdateDateDesc(Long id, Pageable pageable);
    
    /**
     * @param userId
     * @param storyId
     * @return
     */
    Optional<UserFollow> findByUser_IdAndStory_Id(Long userId, Long storyId);

    /**
     * @param userId - Id của người dùng
     * @param storyId- Id của Truyện
     * @return {@code true} nếu tồn tại, nếu không tồn tại thì {@code false}
     */
    boolean existsByUser_IdAndStory_Id(Long userId, Long storyId);
}
