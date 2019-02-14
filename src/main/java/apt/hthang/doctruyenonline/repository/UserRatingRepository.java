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

}
