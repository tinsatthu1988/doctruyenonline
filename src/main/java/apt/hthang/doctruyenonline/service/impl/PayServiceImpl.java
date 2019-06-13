package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Pay;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.projections.PaySummary;
import apt.hthang.doctruyenonline.repository.PayRepository;
import apt.hthang.doctruyenonline.repository.UserRepository;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.utils.ConstantsPayTypeUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 */
@Service
public class PayServiceImpl implements PayService {
    Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
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
    @Transactional(noRollbackFor = HttpMyException.class)
    public boolean savePay(Story story, Chapter chapter, User userSend, User userReceived, Integer vote, Double money, Integer payType) {
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
                        money, vote,
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
    
    /**
     * Đếm số thanh toán của Truyện
     *
     * @param id
     * @return
     */
    @Override
    public Long countPayOfStory(Long id) {
        return payRepository.countByStory_IdOrChapter_Story_Id(id, id);
    }
    
    @Override
    public boolean saveNew(Pay pay) {
        pay = payRepository.save(pay);
        return pay.getId() != null;
    }
    
    @Override
    @Transactional
    public void setPay(User user, Double valueOf) {
        Pay pay = new Pay();
        pay.setUserSend(user);
        pay.setMoney(valueOf);
        pay.setType(ConstantsPayTypeUtils.PAY_WITHDRAW_TYPE);
        pay.setStatus(ConstantsStatusUtils.PAY_WAIT);
        payRepository.save(pay);
        user.setGold(user.getGold() - valueOf);
        userRepository.save(user);
    }
    
    /**
     * Thực Hiện giao dịch đọc chapter Vip
     *
     * @param userSend
     * @param chapter
     */
    @Override
    @Transactional
    public void saveReadingVipPay(User userSend, Chapter chapter) {
        Pay pay = new Pay();
        pay.setUserSend(userSend);
        pay.setChapter(chapter);
        pay.setUserReceived(chapter.getUser());
        pay.setMoney(chapter.getPrice());
        pay.setType(ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE);
        savePay(pay);
        //Lấy Thông Tin Mới Nhất của Người Thanh Toán
        userSend = userRepository.findById(userSend.getId()).get();
        userSend.setGold(userSend.getGold() - chapter.getPrice());
        saveUser(userSend);
        //Lấy Thông tin mới nhất của người nhận
        User userReceived = userRepository.findById(chapter.getUser().getId()).get();
        userSend.setGold(userSend.getGold() - chapter.getPrice());
        userReceived.setGold(userReceived.getGold() + chapter.getPrice());
        saveUser(userReceived);
    }
    
    /**
     * Thực hiện giao dịch đăng ký rút tiền
     *
     * @param user
     * @param money
     */
    @Override
    @Transactional
    public Long savePayDraw(User user, Double money) {
        Pay pay = new Pay();
        pay.setUserSend(user);
        pay.setMoney(money);
        pay.setType(ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE);
        payRepository.save(pay);
        //Lấy Thông Tin Mới Nhất của Người Thanh Toán
        user = userRepository.findById(user.getId()).get();
        user.setGold(user.getGold() - money);
        userRepository.save(user);
        return pay.getId();
    }
    
    /**
     * Thực Hiện Giao Dịch Nạp Tiền cho User
     *
     * @param userSend     - Người Nạp
     * @param money        - Số đậu nạp
     * @param userReceived - Người nhận
     */
    @Override
    @Transactional
    public void savePayChange(User userSend, Double money, User userReceived) {
        Pay pay = new Pay();
        pay.setUserSend(userSend);
        pay.setUserReceived(userReceived);
        pay.setMoney(money);
        pay.setType(ConstantsPayTypeUtils.PAY_RECHARGE_TYPE);
        savePay(pay);
//        payRepository.save(pay);
        //Lấy Thông Tin Mới Nhất của Người Thanh Toán
        userReceived = userRepository.findById(userReceived.getId()).get();
        userReceived.setGold(userReceived.getGold() + money);
        userRepository.save(userReceived);
    }
    
    private void savePay(Pay pay) {
        payRepository.save(pay);
    }
    
    private void saveUser(User user) {
        userRepository.save(user);
    }
}