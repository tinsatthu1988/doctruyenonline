package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Pay;
import apt.hthang.doctruyenonline.projections.PaySummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 */

@Repository
public interface PayRepository extends JpaRepository< Pay, Long > {
    
    /**
     * Thực Hiện Thanh Toán Chapter Vip
     *
     * @param payerID
     * @param receiverID
     * @param chapterID
     * @param price
     * @param payType
     * @return true - nếu thanh toán thành công / false - nếu thanh toán thất bại và roll back dữ liệu
     */
    @Procedure("payChapter")
    boolean transferPayChapter(@Param("userSend") Long payerID,
                               @Param("userReceived") Long receiverID,
                               @Param("chapterID") Long chapterID,
                               @Param("storyID") Long storyID,
                               @Param("price") Double price,
                               @Param("vote") Integer vote,
                               @Param("payType") Integer payType);
    
    /**
     * Kiểm Tra đã tồn tại thanh toán trong khoảng
     *
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     * @param payType
     * @param payStatus
     * @return true - nếu tồn tại/false - nếu không tồn tại
     */
    boolean existsByChapter_IdAndUserSend_IdAndCreateDateBetweenAndTypeAndStatus(Long chID, Long uID, Date startDate, Date endDate, Integer payType, Integer payStatus);
    
    /**
     * Lấy danh sách Giao Dịch Theo
     *
     * @param userReceivedId
     * @param userSendId
     * @param pageable
     * @return
     */
    Page< PaySummary > findByUserReceived_IdOrUserSend_IdOrderByCreateDateDesc(Long userReceivedId, Long userSendId, Pageable pageable);
    
    /**
     * Lấy danh sách Giao Dịch Rút Tiền Theo
     *
     * @param type
     * @param userSendId
     * @param pageable
     * @return
     */
    Page< PaySummary > findByTypeAndUserSend_IdOrderByCreateDateDesc(Integer type, Long userSendId, Pageable pageable);
    
    /**
     * Thực Hiện Từ Chối/ Hủy Giao dịch Rút tiền
     *
     * @param id
     * @return true - nếu thanh toán thành công / false - nếu thanh toán thất bại và roll back dữ liệu
     */
    @Procedure("cancelWithDraw")
    boolean cancelWithDraw(@Param("payId") Long id);
    
    /**
     * Thực Hiện Thanh Toán Đăng ký rút tiền
     *
     * @param userId
     * @param money
     * @param payStatus
     * @param payType
     * @return
     */
    @Procedure("savePayDraw")
    Long saveWithDrawPay(@Param("userId") Long userId,
                            @Param("money") Double money,
                            @Param("payType") Integer payType,
                            @Param("payStatus") Integer payStatus);
    
    /**
     * Đếm số thanh toán của Truyện
     * @param storyId
     * @param id
     * @return
     */
    Long countByStory_IdOrChapter_Story_Id(Long id, Long storyId);
}
