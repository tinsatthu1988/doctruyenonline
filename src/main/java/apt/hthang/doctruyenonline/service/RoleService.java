package apt.hthang.doctruyenonline.service;

import java.util.List;

import apt.hthang.doctruyenonline.entity.Role;

/**
 * @author Huy Thang
 */
public interface RoleService {

    /**
     * Lấy Toàn Bộ Danh sách Phân Quyền
     * @return List<Role>
     */
    List<Role> getAllRole();
}
