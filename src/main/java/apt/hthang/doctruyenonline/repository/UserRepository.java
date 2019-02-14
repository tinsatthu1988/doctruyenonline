package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Huy Thang
 */
@Repository
public interface UserRepository extends JpaRepository< User, Long > {

    /**
     * Tìm kiếm User theo username
     *
     * @param userName
     * @return Optional<User>
     */
    Optional< User > findByUsername(String userName);
}
