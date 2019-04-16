package apt.hthang.doctruyenonline.repository;

import apt.hthang.doctruyenonline.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Huy Thang
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
                               @Param("payType") Integer payType);
}
