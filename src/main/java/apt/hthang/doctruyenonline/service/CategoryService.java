package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.CategorySummary;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Huy Thang
 */
public interface CategoryService extends FieldValueExists {
    
    /**
     * Lấy danh sách Thể loại của Menu
     *
     * @param status
     * @return List<CategorySummary> - danh sách thể loại
     */
    List< CategorySummary > getListCategoryOfMenu(Integer status);
    
    /**
     * Tìm Category theo Id và status
     *
     * @param id
     * @param status
     * @return CategorySummary - nếu tồn tại
     * @throws Exception - nếu không tồn tại category có id và status
     */
    CategorySummary getCategoryByID(Integer id, Integer status) throws Exception;
    
    Page<Category> findCategoryBySearch(String search, Integer pagenumber, Integer size);
    
    /**
     *
     * @param id
     * @return
     */
    Category findCategoryById(Integer id);
    
    boolean deleteCategory(Category category);
    
    boolean newCategory(Category category);
}
