package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri/the_loai")
public class AdminCategoryController {
    
    private final Logger logger = LoggerFactory.getLogger(AdminCategoryController.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private MyComponent myComponent;
    
    // Lấy Thông Tin Tên User Login, Avatar User Login
    private void getUser(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        model.addAttribute("avatar", myComponent.checkAvatar(user.getAvatar()));
        model.addAttribute("displayname", myComponent.getDisplayName(user.getUsername(), user.getDisplayName()));
        model.addAttribute("titleMenu", "Quản Lý Thể Loại");
    }
    
    @RequestMapping
    public String defaultAdmiHome(Model model, Principal principal) throws Exception {
        LocalDate today = LocalDate.now();
        model.addAttribute("title", "Danh Sách Thể Loại");
        
        getUser(model, principal);
        
        return "/dashboard/adminCategoryPage";
    }
    
    @GetMapping("/them_moi")
    public String addStoryPage(Model model, Principal principal) throws Exception {
        
        model.addAttribute("title", "Thêm Thể Loại");
        
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", new Category());
        }
        
        getUser(model, principal);
        
        return "dashboard/addCategoryPage";
    }
    
    @PostMapping("/them_moi/save")
    public String saveStoryPage(@Valid Category category, BindingResult result, Principal principal,
                                RedirectAttributes redirectAttributes) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        boolean hasError = result.hasErrors();
        if (hasError) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("category", category);
            return "redirect:/quan-tri/the_loai/them_moi";
        }
        category.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        category.setCreateBy(user.getUsername());
        boolean check = categoryService.newCategory(category);
        redirectAttributes.addFlashAttribute("checkAddCategory", check);
        
        return "redirect:/quan-tri/the_loai";
    }
    
    @GetMapping("/cap_nhat/{id}")
    public String editStoryPage(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttrs,
                                Principal principal) throws Exception {
        
        model.addAttribute("title", "Thêm Thể Loại");
        Category category = categoryService.findCategoryById(id);
        if (category == null) {
            redirectAttrs.addFlashAttribute("checkEditCategoryFalse", "Thể Loại không tồn tại");
            return "redirect:/quan-tri/the_loai";
        }
        
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", category);
        }
        model.addAttribute("statusList", ConstantsListUtils.LIST_CATEGORY_STATUS_VIEW_ALL);
        
        getUser(model, principal);
        
        return "dashboard/editCategoryPage";
    }
    
    @PostMapping("/cap_nhat/save")
    public String saveEditStoryPage(@Valid Category category, BindingResult result, Principal principal,
                                    RedirectAttributes redirectAttributes) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        if (categoryService.exitsCategoryName(category.getId(), category.getName()) && category.getName() != null
                && !category.getName().isEmpty())
            result.addError(new FieldError("category", "name", "Đã tồn tại Thể Loại có tên này"));
        boolean hasError = result.hasErrors();
        if (hasError) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.category", result);
            redirectAttributes.addFlashAttribute("category", category);
            return "redirect:/quan-tri/the_loai/cap_nhat/" + category.getId();
        }
        category.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        boolean check = categoryService.newCategory(category);
        redirectAttributes.addFlashAttribute("checkEditCategory", check);
        return "redirect:/quan-tri/the_loai";
    }
}
