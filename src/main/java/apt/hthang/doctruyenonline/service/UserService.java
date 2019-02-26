package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.User;

/**
 * @author Huy Thang
 */
public interface UserService {

    /**
     * Tìm kiếm User theo username
     * @param userName
     * @return User - Nếu tồn tại user với userName / null - nếu không tồn tại user với userName
     */
    User findUserAccount(String userName);
    
    /**
     * Đăng ký người dùng mới
     * @param user
     * @return true - nếu đăng ký thành công / false - nếu có lỗi xảy ra
     */
    boolean registerUser(User user);
}
