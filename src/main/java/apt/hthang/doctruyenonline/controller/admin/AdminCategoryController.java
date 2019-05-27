package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.entity.Category;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.CategoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri")
public class AdminCategoryController {
    
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    
    @RequestMapping("/the_loai")
    public String defaultAdmiHome(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("title", "Danh Sách Thể Loại");
        return "/dashboard/adminCategoryPage";
    }
    
    @GetMapping("/them_the_loai")
    public String addStoryPage(Model model) {
        
        model.addAttribute("title", "Thêm Thể Loại");
        
        if (!model.containsAttribute("category")) {
            model.addAttribute("category", new Category());
        }
        
        return "dashboard/addCategoryPage";
    }
    
    @PostMapping("/them_the_loai/save")
    public String saveStoryPage(@Valid Category category, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) throws NotFoundException {
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
            return "redirect:/quan-tri/them_the_loai";
        }
        category.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        category.setCreateBy(user.getUsername());
        boolean check = categoryService.newCategory(category);
        redirectAttributes.addFlashAttribute("checkAddCategory", check);
        
        return "redirect:/quan-tri/the_loai";
    }
    
    @GetMapping("/sua_the_loai/{id}")
    public String editStoryPage(Model model, @PathVariable("id") Integer id, RedirectAttributes redirectAttrs) {
        
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
        return "dashboard/editCategoryPage";
    }
    
    @PostMapping("/sua_the_loai/save")
    public String saveEditStoryPage(@Valid Category category, BindingResult result, Principal principal, RedirectAttributes redirectAttributes) throws NotFoundException {
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
            return "redirect:/quan-tri/sua_the_loai/" + category.getId();
        }
        category.setMetatitle(WebUtils.convertStringToMetaTitle(category.getName()));
        category.setModifiedBy(user.getUsername());
        boolean check = categoryService.newCategory(category);
        redirectAttributes.addFlashAttribute("checkEditCategory", check);
        
        return "redirect:/quan-tri/the_loai";
    }
}
