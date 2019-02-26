package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.repository.CategoryRepository;
import apt.hthang.doctruyenonline.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Huy Thang
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    /**
     * Lấy danh sách Thể loại của Menu
     *
     * @param status
     * @return List<Category> - danh sách thể loại
     */
    @Override
    public List< Category > getListCategoryOfMenu(Integer status) {
        return categoryRepository.findAllByStatus(status);
    }
    
    /**
     * Tìm Category theo Id và status
     *
     * @param id
     * @param status
     * @return category - nếu tồn tại
     * @throws NotFoundException - nếu không tồn tại category có id và status
     */
    @Override
    public Category getCategoryByID(Integer id, Integer status) throws NotFoundException {
        Optional< Category > category = categoryRepository.findByIdAndStatus(id, status);
        return category.orElseThrow(NotFoundException::new);
    }
}
