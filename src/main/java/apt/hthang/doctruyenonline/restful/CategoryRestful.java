package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/category")
public class CategoryRestful {
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping(value = "/list")
    public ResponseEntity< ? > loadCategory(@RequestParam("content") String contentEncryp,
                                            @RequestParam("pagenumber") Integer pagenumber,
                                            Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpUserLockedException();
        }
        return new ResponseEntity<>(categoryService.findCategoryBySearch(contentEncryp
                , pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity< ? > deletePayDraw(@PathVariable("id") Integer id,
                                             Principal principal) throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpUserLockedException();
        }
        Category category = categoryService.findCategoryById(id);
        if (category == null)
            throw new HttpMyException("Thể Loại Không Tồn tại!");
        boolean result = categoryService.deleteCategory(category);
        if (result)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new HttpMyException("Không Thể Xóa Thể Loại!");
    }
    
}

