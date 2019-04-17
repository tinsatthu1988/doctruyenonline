package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.repository.PayRepository;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.utils.ConstantsPayTypeUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param chID
     * @param uID
     * @param startDate
     * @param endDate
     * @return true - nếu đã thanh toán trong khoảng /false - nếu chưa thanh toán / hoặc thanh toán ngoài khoảng
     */
    @Override
    public boolean checkDealChapterVip(Long chID, Long uID, Date startDate, Date endDate) {
        return payRepository
                .existsByChapter_IdAndUserSend_IdAndCreateDateBetweenAndTypeAndStatus(chID, uID,
                        startDate, endDate, ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE, ConstantsStatusUtils.PAY_COMPLETED);
    }
}