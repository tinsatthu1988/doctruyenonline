package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.exception.NotFoundException;

import java.util.List;

/**
 * @author Huy Thang
 */
public interface CategoryService {
    
    /**
     * Lấy danh sách Thể loại của Menu
     *
     * @param status
     * @return List<Category> - danh sách thể loại
     */
    List< Category > getListCategoryOfMenu(Integer status);
    
    /**
     * Tìm Category theo Id và status
     *
     * @param id
     * @param status
     * @return category - nếu tồn tại
     * @throws Exception - nếu không tồn tại category có id và status
     */
    Category getCategoryByID(Integer id, Integer status) throws Exception;
}
