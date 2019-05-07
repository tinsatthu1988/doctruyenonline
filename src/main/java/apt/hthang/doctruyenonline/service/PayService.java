package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Pay;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.PaySummary;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * @author Huy Thang
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
                    User userSend, User userReceived,
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
     * @param id - id của User
     * @param pagenumber - biến số trang
     * @param size - biến size
     * @return
     */
    Page< PaySummary > findPageByIdAndDate(Long id, Integer pagenumber, Integer size);
}
