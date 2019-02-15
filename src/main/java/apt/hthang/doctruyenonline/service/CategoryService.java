package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Category;

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

}
