package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.repository.RoleRepository;
import apt.hthang.doctruyenonline.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Huy Thang
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

}
