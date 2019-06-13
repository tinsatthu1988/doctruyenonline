package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.projections.CategorySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Đời Không Như Là Mơ
 */
@Repository
public interface CategoryRepository extends JpaRepository< Category, Integer > {
    
    /**
     * Lấy danh sách Thể loại theo
     *
     * @param stauts
     * @return List<CategorySummary> - danh sách thể loại
     */
    List< CategorySummary > findAllByStatus(Integer stauts);
    
    /**
     * Tìm category theo id và status
     *
     * @param id
     * @param status
     * @return Optional<CategorySummary>
     */
    Optional< CategorySummary > findByIdAndStatus(Integer id, Integer status);
    
    Page< Category > findAllByNameContaining(String search, Pageable pageable);
    
    /**
    *   Kiểm Tra đã tồn tại thể loại có tên
    *   @param name
    *
    * @return true - nếu đã tồn tại name / false - nếu chưa tồn tại tên
    */
    boolean existsByNameIgnoreCase(String name);
    
    /**
     * Kiểm Tra đã tồn tại thể loại có tên với Id khác
     * @param name
     * @param id
     * @return
     */
    boolean existsByNameIgnoreCaseAndIdNot(String name, Integer id);
}
