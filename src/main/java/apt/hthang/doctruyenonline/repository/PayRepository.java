package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */

@Repository
public interface PayRepository extends JpaRepository< Pay, Long > {

}
