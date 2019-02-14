package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface RoleRepository extends JpaRepository< Role, Integer > {

}
