package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Pay;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.PaySummary;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 */
public interface PayService {
    
    /**
     * Lưu đề cử truyện
     *
     * @param story
     * @param chapter
     * @param userSend
     * @param userReceived
     * @param money
     * @param payType
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    boolean savePay(Story story, Chapter chapter,
                    User userSend, User userReceived, Integer vote,
                    Double money, Integer payType);
    
    /**
     * Kiểm tra User đã thanh toán Chapter Vip trong khoảng
     *
     * @param chapterId
     * @param userId
     * @param startDate
     * @param endDate
     * @return true - nếu đã thanh toán trong khoảng /false - nếu chưa thanh toán / hoặc thanh toán ngoài khoảng
     */
    boolean checkDealChapterVip(Long chapterId, Long userId, Date startDate, Date endDate);
    
    /**
     * Lấy danh sách giao dịch của User theo
     *
     * @param id         - id của User
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @return
     */
    Page< PaySummary > findPageByUserId(Long id, Integer pagenumber, Integer size);
    
    /**
     * Lấy danh sách giao dịch rút tiền của người dùng
     *
     * @param id
     * @param pagenumber
     * @param size
     * @return
     */
    Page< PaySummary > findPagePayWithdrawByUserId(Long id, Integer pagenumber, Integer size);
    
    /**
     * Tìm kiếm Pay Theo id
     *
     * @param payId - id Pay
     * @return
     */
    Pay findPayById(Long payId);
    
    /**
     * Thực Hiện Lệnh Hủy/ Từ Chối Giao dịch
     *
     * @param id
     */
    boolean cancelWithDraw(Long id);
    
    /**
     * Tạo mới đăng ký rút tiền
     *
     * @param userId
     * @param money
     * @return
     */
    Long newPayWithDraw(Long userId, Double money);
    
    /**
     * Đếm số thanh toán của Truyện
     *
     * @param id
     * @return
     */
    Long countPayOfStory(Long id);
    
    /**
     * Thực Hiện giao dịch đọc chapter Vip
     *
     * @param userSend
     * @param chapter
     */
    void saveReadingVipPay(User userSend, Chapter chapter);
    
    /**
     * Thực hiện giao dịch đăng ký rút tiền
     *
     * @param user
     * @param money
     */
    Long savePayDraw(User user, Double money);
    
    /**
     * Thực Hiện Giao Dịch Nạp Tiền cho User
     *
     * @param userSend     - Người Nạp
     * @param money        - Số đậu nạp
     * @param userReceived - Người nhận
     */
    void savePayChange(User userSend, Double money, User userReceived);
    
    boolean saveNew(Pay pay);
    
    void setPay(User user, Double valueOf);
}
