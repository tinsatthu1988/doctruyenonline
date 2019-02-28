package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Role;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.repository.RoleRepository;
import apt.hthang.doctruyenonline.repository.UserRepository;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsRoleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Huy Thang
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;
    
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    
    /**
     * Tìm kiếm User theo username
     *
     * @param userName
     * @return User - Nếu tồn tại user với userName / null - nếu không tồn tại user với userName
     */
    @Override
    public User findUserAccount(String userName) {
        return userRepository
                .findByUsername(userName)
                .orElse(null);
    }
    
    /**
     * Đăng ký người dùng mới
     *
     * @param user
     * @return true - nếu đăng ký thành công / false - nếu có lỗi xảy ra
     */
    @Override
    @Transactional
    public boolean registerUser(User user) {
        List< Role > roleList = new ArrayList<>();
        Role role = roleRepository.findById(ConstantsRoleUtils.ROLE_USER_ID).get();
        roleList.add(role);
        user.setRoleList(roleList);
        User newUser = userRepository.save(user);
        return newUser.getId() != null;
    }
    
    /**
     * Tìm User Theo UserName và Email
     *
     * @param userName
     * @param email
     * @return User - Nếu tồn tại user với userName và Email / null - nếu không tồn tại user với userName và Email
     */
    @Override
    public User findForgotUser(String userName, String email) {
        return userRepository
                .findByUsernameAndEmail(userName, email)
                .orElse(null);
    }
    
    /**
     * Cập Nhật User
     *
     * @param user
     * @return User
     */
    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
