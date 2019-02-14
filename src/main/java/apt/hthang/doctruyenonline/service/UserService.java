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
}
