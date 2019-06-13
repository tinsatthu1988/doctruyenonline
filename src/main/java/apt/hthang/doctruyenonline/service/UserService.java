package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.projections.InfoSummary;
import apt.hthang.doctruyenonline.projections.TopConverter;
import apt.hthang.doctruyenonline.projections.UserAdmin;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author Đời Không Như Là Mơ
 */
public interface UserService extends FieldValueExists {
    
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
    
    /**
     * Lấy Thông Tin Người dùng Theo id
     *
     * @param id
     * @return
     */
    InfoSummary findInfoUserById(Long id);
    
    /**
     * @param page
     * @param size
     * @return
     */
    List< TopConverter > findTopConverter(int page, int size);
    
    ;
    
    /**
     * @param date
     * @return
     */
    Long countUserNewInDate(Date date);
    
    Page< UserAdmin > findByType(String search, Integer type, Integer pagenumber, Integer size);
    
    void deleteUser(User deleteUser);
    
    boolean updateGoldUser(Long id, Double gold);
}
