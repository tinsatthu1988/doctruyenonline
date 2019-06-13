package apt.hthang.doctruyenonline.service.impl;

import java.util.List;

import apt.hthang.doctruyenonline.entity.Role;
import apt.hthang.doctruyenonline.repository.RoleRepository;
import apt.hthang.doctruyenonline.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Đời Không Như Là Mơ
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

     /**
     * Lấy Toàn Bộ Danh sách Phân Quyền
     * @return List<Role>
     */
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

}
