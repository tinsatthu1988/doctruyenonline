package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
 */
@Repository
public interface CategoryRepository extends JpaRepository< Category, Integer > {

}
