package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface InformationRepository extends JpaRepository< Information, Integer > {

}
