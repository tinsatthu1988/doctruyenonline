package apt.hthang.doctruyenonline.controller.admin;

import apt.hthang.doctruyenonline.component.MyComponent;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.RoleService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
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

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Controller
@PropertySource(value = "classpath:messages.properties", encoding = "UTF-8")
@RequestMapping(value = "/quan-tri/nguoi_dung")
public class AdminUserController {
    
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private MyComponent myComponent;
    
    //Lấy Thông Tin Tên User Login, Avatar User Login
    private void getUser(Model model, Principal principal) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        model.addAttribute("checkRole", myComponent.hasRole(user, ConstantsUtils.ROLE_ADMIN));
        model.addAttribute("avatar", myComponent.checkAvatar(user.getAvatar()));
        model.addAttribute("displayname", myComponent.getDisplayName(user.getUsername(), user.getDisplayName()));
        model.addAttribute("titleMenu", "Quản Lý Người Dùng");
    }
    
    @RequestMapping
    public String defaultAdminHome(Model model, Principal principal) throws Exception {
        
        model.addAttribute("title", "Danh Sách Người Dùng");
        
        getUser(model, principal);
        
        return "/dashboard/adminUserPage";
    }
    
    @GetMapping("/cap_nhat/{id}")
    public String updateUser(Model model, Principal principal, @PathVariable("id") Long id, RedirectAttributes redirectAttrs) throws Exception {
        
        model.addAttribute("title", "Cập Nhật Người Dùng");
        
        getUser(model, principal);
        
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = userService.findUserById(loginedUser.getUser().getId());
        if (user == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        User editUser = userService.findUserById(id);
        
        if (editUser == null) {
            redirectAttrs.addFlashAttribute("checkEditUserFalse", "Người dùng không tồn tại");
            return "redirect:/quan-tri/nguoi_dung";
        }
        
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", editUser);
        }
        
        model.addAttribute("listRole", roleService.getAllRole());
        
        model.addAttribute("statusList", ConstantsListUtils.LIST_CATEGORY_STATUS_VIEW_ALL);
        
        return "dashboard/editUserPage";
    }
    
    @PostMapping("/cap_nhat/save")
    public String saveUpdateUser(@Valid User user, BindingResult result, Principal principal,
                                 RedirectAttributes redirectAttributes) throws NotFoundException {
        MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User userLogin = userService.findUserById(loginedUser.getUser().getId());
        if (userLogin == null) {
            throw new NotFoundException("Tài khoản không tồn tại mời liên hệ admin để biết thêm thông tin");
        }
        if (userLogin.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new NotFoundException("Tài khoản của bạn đã bị khóa mời liên hệ admin để biết thêm thông tin");
        }
        
        User editUser = userService.findUserById(user.getId());
        
        if (editUser == null) {
            redirectAttributes.addFlashAttribute("checkEditUserFalse", "Người dùng cập nhật không tồn tại");
            return "redirect:/quan-tri/nguoi_dung";
        }
        
        boolean hasError = result.hasErrors();
        if (hasError) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/quan-tri/nguoi_dung/cap_nhat/" + user.getId();
        }
        editUser.setStatus(user.getStatus());
        editUser.setRoleList(user.getRoleList());
        try {
            userService.updateUser(editUser);
            redirectAttributes.addFlashAttribute("checkEditUserTrue", "Cập Nhật Thành Công");
            return "redirect:/quan-tri/nguoi_dung";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("checkEditUserFalse", "Có lỗi xảy ra! Cập Nhật Không Thành Công!");
            return "redirect:/quan-tri/nguoi_dung";
        }
        
    }
}
