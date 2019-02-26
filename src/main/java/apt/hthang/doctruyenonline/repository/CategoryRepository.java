package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    
    /**
     * Tìm category theo id và status
     *
     * @param id
     * @param status
     * @return Optional<Category>
     */
    Optional< Category > findByIdAndStatus(Integer id, Integer status);
}
