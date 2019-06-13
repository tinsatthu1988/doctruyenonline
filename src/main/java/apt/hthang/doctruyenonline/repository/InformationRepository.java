package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 */
@Repository
public interface InformationRepository extends JpaRepository< Information, Integer > {

    /**
     * Lấy Information đầu tiên
     * @return Optional<Information>
     */
    Optional<Information> findFirstByOrderByIdDesc();

}
