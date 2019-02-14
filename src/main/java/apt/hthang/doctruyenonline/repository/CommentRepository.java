package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface CommentRepository extends JpaRepository< Comment, Long > {
}
