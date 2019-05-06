package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.projections.StoryUpdate;
import apt.hthang.doctruyenonline.projections.StoryUser;
import org.springframework.data.domain.Page;

/**
 * @author Huy Thang
 */
public interface UserService {
    
    /**
     * Tìm kiếm User theo username
     *
     * @param userName
     * @return User - Nếu tồn tại user với userName / null - nếu không tồn tại user với userName
     */
    User findUserAccount(String userName);
    
    /**
     * Đăng ký người dùng mới
     *
     * @param user
     * @return true - nếu đăng ký thành công / false - nếu có lỗi xảy ra
     */
    boolean registerUser(User user);
    
    /**
     * Tìm User Theo UserName và Email
     *
     * @param userName
     * @param email
     * @return User
     */
    User findForgotUser(String userName, String email);
    
    /**
     * Cập Nhật User
     *
     * @param user
     * @return User
     */
    User updateUser(User user);
    
    /**
     * Tìm user theo Id
     *
     * @param id
     * @return User - nếu tồn tại / null- nếu không tồn tại user
     */
    User findUserById(Long id);
    
    /**
     * Lấy Thông Tin Converter
     *
     * @param id
     * @return ConverterSummary
     */
    ConveterSummary findConverterByID(Long id);
    
    /**
     * Kiểm tra DisplayName đã tồn tại chưa
     *
     * @param userId
     * @param newNick
     * @return boolean
     */
    boolean checkUserDisplayNameExits(Long userId, String newNick);
    
    /**
     * Cập nhật ngoại hiệu
     *
     * @param userId
     * @param money
     * @param newNick
     */
    void updateDisplayName(Long userId, Double money, String newNick) throws Exception;
    
}
