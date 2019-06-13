package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.projections.CategorySummary;
import apt.hthang.doctruyenonline.repository.CategoryRepository;
import apt.hthang.doctruyenonline.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Đời Không Như Là Mơ
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
    
    @Override
    public Page< Category > findCategoryBySearch(String search, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size, Sort.by("id").descending());
        if (search.trim().length() == 0) {
            return categoryRepository.findAll(pageable);
        }
        return categoryRepository.findAllByNameContaining(search, pageable);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    public Category findCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }
    
    @Override
    public boolean deleteCategory(Category category) {
        try {
            categoryRepository.deleteById(category.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean newCategory(Category category) {
        category = categoryRepository.save(category);
        return category.getId() != null;
    }
    
    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param value     The value to check for
     * @param fieldName The name of the field for which to check if the value exists
     * @return True if the value exists for the field; false otherwise
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        Assert.notNull(fieldName);
        
        if (!fieldName.equals("name")) {
            throw new UnsupportedOperationException("Field name not supported");
        }
        
        if (value == null) {
            return true;
        }
        return this.categoryRepository.existsByNameIgnoreCase(value.toString());
    }
    
    /**
     * Kiểm tra sự tồn tại của Thể loại theo name và Id
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public boolean exitsCategoryName(Integer id, String name) {
        return categoryRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }
}

