package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Role;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.projections.InfoSummary;
import apt.hthang.doctruyenonline.projections.TopConverter;
import apt.hthang.doctruyenonline.projections.UserAdmin;
import apt.hthang.doctruyenonline.repository.RoleRepository;
import apt.hthang.doctruyenonline.repository.UserRepository;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsRoleUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Đời Không Như Là Mơ
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
    
    /**
     * Tìm user theo Id
     *
     * @param id
     * @return User - nếu tồn tại / null- nếu không tồn tại user
     */
    @Override
    public User findUserById(Long id) {
        return userRepository
                .findById(id)
                .orElse(null);
    }
    
    /**
     * Lấy Thông Tin Converter
     *
     * @param id
     * @return ConverterSummary
     */
    @Override
    public ConveterSummary findConverterByID(Long id) {
        return userRepository.findUserById(id);
    }
    
    /**
     * Kiểm tra DisplayName đã tồn tại chưa
     *
     * @param userId
     * @param newNick
     * @return boolean
     */
    @Override
    public boolean checkUserDisplayNameExits(Long userId, String newNick) {
        return userRepository.existsByIdNotAndDisplayName(userId, newNick);
    }
    
    /**
     * Cập nhật ngoại hiệu
     *
     * @param userId
     * @param money
     * @param newNick
     */
    @Override
    public void updateDisplayName(Long userId, Double money, String newNick) throws Exception {
        User user = userRepository.findById(userId).get();
        if (user.getGold() < money)
            throw new HttpMyException("Số dư của bạn không đủ để thanh toán!");
        user.setDisplayName(newNick);
        user.setGold(user.getGold() - money);
        userRepository.save(user);
    }
    
    /**
     * Lấy Thông Tin Người dùng Theo id
     *
     * @param id
     * @return
     */
    @Override
    public InfoSummary findInfoUserById(Long id) {
        return userRepository.findUsersById(id).orElse(null);
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
        
        if (!fieldName.equals("email") && !fieldName.equals("username")) {
            throw new UnsupportedOperationException("Field name not supported");
        }
        
        if (value == null) {
            return true;
        }
        if (fieldName.equals("email")) {
            return this.userRepository.existsUserByEmailIgnoreCase(value.toString());
        }
        return this.userRepository.existsUserByUsernameIgnoreCase(value.toString());
    }
    
    @Override
    public List< TopConverter > findTopConverter(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page< TopConverter > result = userRepository
                .getTopConverter(ConstantsListUtils.LIST_CHAPTER_DISPLAY,
                        ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsStatusUtils.USER_ACTIVED, ConstantsListUtils.LIST_ROLE_CON, pageable);
        return result.getContent();
    }
    
    /**
     * @param date
     * @return
     */
    @Override
    public Long countUserNewInDate(Date date) {
        return userRepository.countByCreateDateGreaterThanEqual(date);
    }
    
    @Override
    public Page< UserAdmin > findByType(String search, Integer type, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        Role role = roleRepository.findById(type).orElse(null);
        if (search.trim().length() != 0)
            return userRepository.findByUsernameContainingAndRoleList(search, role, pageable);
        return userRepository.findByRoleList(role, pageable);
        
    }
    
    @Override
    public void deleteUser(User deleteUser) {
            userRepository.delete(deleteUser);
    }
    
    @Override
    public boolean updateGoldUser(Long id, Double gold) {
        try {
            User user = userRepository.findById(id).get();
            user.setGold(user.getGold() + gold);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
