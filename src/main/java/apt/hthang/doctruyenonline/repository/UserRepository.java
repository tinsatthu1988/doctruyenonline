package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Role;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.projections.InfoSummary;
import apt.hthang.doctruyenonline.projections.TopConverter;
import apt.hthang.doctruyenonline.projections.UserAdmin;
import apt.hthang.doctruyenonline.utils.ConstantsQueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
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
    boolean existsUserByEmailIgnoreCase(String email);
    
    /**
     * Kiểm Tra đã tồn tại username
     *
     * @param username
     * @return
     */
    boolean existsUserByUsernameIgnoreCase(String username);
    
    @Query(value = ConstantsQueryUtils.TOP_CONVERTER,
            countQuery = ConstantsQueryUtils.COUNT_TOP_CONVERTER,
            nativeQuery = true)
    Page< TopConverter > getTopConverter(@Param("chapterStatus") List< Integer > listChapterStatus,
                                         @Param("storyStatus") List< Integer > listStoryStatus,
                                         @Param("userStatus") Integer uStatus, @Param("roleList") List< Integer > listRole, Pageable pageable);
    
    /**
     * @param date
     * @return
     */
    Long countByCreateDateGreaterThanEqual(Date date);
    
    Page< UserAdmin > findByRoleList(Role role, Pageable pageable);
    
    Page< UserAdmin > findByUsernameContainingAndRoleList(String search, Role role, Pageable pageable);

}
