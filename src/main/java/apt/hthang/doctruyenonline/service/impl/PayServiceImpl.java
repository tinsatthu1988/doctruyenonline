package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Pay;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.projections.PaySummary;
import apt.hthang.doctruyenonline.repository.PayRepository;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.utils.ConstantsPayTypeUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Huy Thang
 */
@Service
public class PayServiceImpl implements PayService {
    
    @Autowired
    private PayRepository payRepository;
    
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
    @Override
    public boolean savePay(Story story, Chapter chapter, User userSend, User userReceived, Double money, Integer payType) {
        Long chapterID = null;
        Long storyID = null;
        if (chapter != null)
            chapterID = chapter.getId();
        if (story != null)
            storyID = story.getId();
        return payRepository
                .transferPayChapter(userSend.getId(),
                        userReceived.getId(),
                        chapterID,
                        storyID,
                        money,
                        payType);
    }
    
    /**
     * Kiểm tra User đã thanh toán Chapter Vip trong khoảng
     *
     * @param chapterId
     * @param userId
     * @param startDate
     * @param endDate
     * @return true - nếu đã thanh toán trong khoảng /false - nếu chưa thanh toán / hoặc thanh toán ngoài khoảng
     */
    @Override
    public boolean checkDealChapterVip(Long chapterId, Long userId, Date startDate, Date endDate) {
        return payRepository
                .existsByChapter_IdAndUserSend_IdAndCreateDateBetweenAndTypeAndStatus(chapterId, userId,
                        startDate, endDate, ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE, ConstantsStatusUtils.PAY_COMPLETED);
    }
    
    /**
     * Lấy danh sách giao dịch của User theo
     *
     * @param id         - id của User
     * @param pagenumber - biến số trang
     * @param size       - biến size
     * @return
     */
    @Override
    public Page< PaySummary > findPageByUserId(Long id, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return payRepository.findByUserReceived_IdOrUserSend_IdOrderByCreateDateDesc(id, id, pageable);
    }
    
    /**
     * Lấy danh sách giao dịch rút tiền của người dùng
     *
     * @param id
     * @param pagenumber
     * @param size
     * @return
     */
    @Override
    public Page< PaySummary > findPagePayWithdrawByUserId(Long id, Integer pagenumber, Integer size) {
        Pageable pageable = PageRequest.of(pagenumber - 1, size);
        return payRepository.findByTypeAndUserSend_IdOrderByCreateDateDesc(ConstantsPayTypeUtils.PAY_WITHDRAW_TYPE, id, pageable);
    }
    
    /**
     * Tìm kiếm Pay Theo id
     *
     * @param payId - id Pay
     * @return
     */
    @Override
    public Pay findPayById(Long payId) {
        return payRepository.findById(payId).orElse(null);
    }
    
    /**
     * Thực Hiện Lệnh Hủy/ Từ Chối Giao dịch
     *
     * @param id
     */
    @Override
    public boolean cancelWithDraw(Long id) {
        return payRepository.cancelWithDraw(id);
    }
    
    /**
     * Tạo mới đăng ký rút tiền
     *
     * @param userId
     * @param money
     * @return
     */
    @Override
    public Long newPayWithDraw(Long userId, Double money) {
        return payRepository.saveWithDrawPay(userId, money, ConstantsPayTypeUtils.PAY_WITHDRAW_TYPE, ConstantsStatusUtils.PAY_WAIT);
    }
}