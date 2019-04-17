package apt.hthang.doctruyenonline.exception;

import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.InformationService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private final InformationService informationService;
    
    private final CategoryService categoryService;
    
    @Autowired
    public GlobalExceptionHandler(InformationService informationService, CategoryService categoryService) {
        this.informationService = informationService;
        this.categoryService = categoryService;
    }
    
    private void getMenuAndInfo(ModelAndView modelAndView, String title) {
        
        // Lấy Title Cho Page
        modelAndView.addObject("title", title);
        
        // Lấy List Category cho Menu
        modelAndView.addObject("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        modelAndView.addObject("information", informationService.getWebInfomation());
    }
    
    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView myError(NotFoundException ex) {
        ModelAndView mav = new ModelAndView("web/view/ErrorPage.html");
        mav.addObject("number", "404");
        mav.addObject("message", ex.getMessage());
        getMenuAndInfo(mav, "Trang này không tồn tại");
        
        return mav;
    }
    
    @ExceptionHandler(HttpNotLoginException.class)
    public final ResponseEntity< Object > handleUserHttpNotLoginException(HttpNotLoginException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(HttpUserLockedException.class)
    public final ResponseEntity< Object > handleHttpUserLockedException(HttpUserLockedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(HttpMyException.class)
    public final ResponseEntity< Object > handleHttpMyException(HttpMyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}