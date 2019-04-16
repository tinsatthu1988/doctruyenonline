package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.repository.PayRepository;
import apt.hthang.doctruyenonline.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}