package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;

/**
 * @author Huy Thang
 */
public interface PayService {
    
    /**
     * Lưu đề cử truyện
     * @param story
     * @param chapter
     * @param userSend
     * @param userReceived
     * @param money
     * @param payType
     * @return true - nếu thành công
     * @return false - nếu thất bại hoặc có lỗi xảy ra
     */
    boolean savePay(Story story, Chapter chapter,
                    User userSend, User userReceived,
                    Double money, Integer payType);
}
