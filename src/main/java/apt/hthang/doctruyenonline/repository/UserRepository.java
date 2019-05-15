package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.projections.InfoSummary;
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
     *
     * @param id
     * @return ConveterSummary
     */
    ConveterSummary findUserById(Long id);
    
    /**
     * Tìm User theo id
     *
     * @param id
     * @return InfoSummary
     */
    Optional< InfoSummary > findUsersById(Long id);
    
    /**
     * Kiểm Tra Có tồn tại Display Name với điều kiện Khác userId không
     *
     * @param userId
     * @param newNick
     * @return true - nếu tồn tại user/ false - nếu không tồn tại user
     */
    boolean existsByIdNotAndDisplayName(Long userId, String newNick);
    
    /**
     * @param userId
     * @param encrypString
     * @return
     */
    boolean existsByIdAndPassword(Long userId, String encrypString);
    
    /**
     * Kiểm Tra đã tồn tại email
     *
     * @param email
     * @return
     */
    boolean existsUserByEmail(String email);
    
    /**
     * Kiểm Tra đã tồn tại username
     *
     * @param username
     * @return
     */
    boolean existsUserByUsername(String username);
}
