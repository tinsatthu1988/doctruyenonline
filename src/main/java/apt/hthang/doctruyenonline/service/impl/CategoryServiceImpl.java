package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.CategorySummary;
import apt.hthang.doctruyenonline.repository.CategoryRepository;
import apt.hthang.doctruyenonline.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List< CategorySummary > getListCategoryOfMenu(Integer status) {
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
    public CategorySummary getCategoryByID(Integer id, Integer status) throws NotFoundException {
        return categoryRepository
                .findByIdAndStatus(id, status)
                .orElseThrow(NotFoundException::new);
    }
}
