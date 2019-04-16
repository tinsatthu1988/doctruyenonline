package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
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
    
    /**
     * Tìm User theo username và email
     *
     * @param userName
     * @param email
     * @return Optional<User>
     */
    Optional< User > findByUsernameAndEmail(String userName, String email);
    
    /**
     * Tìm User theo id
     * @param id
     * @return ConveterSummary
     */
    ConveterSummary findUserById(Long id);
}
