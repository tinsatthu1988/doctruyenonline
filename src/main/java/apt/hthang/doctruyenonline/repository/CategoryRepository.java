package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huy Thang
 */
@Repository
public interface CategoryRepository extends JpaRepository< Category, Integer > {

    /**
     * Lấy danh sách Thể loại theo
     *
     * @param stauts
     * @return List<Category> - danh sách thể loại
     */
    List< Category > findAllByStatus(Integer stauts);
}
