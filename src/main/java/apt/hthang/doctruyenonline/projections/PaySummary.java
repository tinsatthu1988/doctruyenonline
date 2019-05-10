package apt.hthang.doctruyenonline.projections;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author Linh
 * @project doctruyenonline
 */
public interface PaySummary {
    //Id giao dịch
    Long getId();
    
    //Id người gửi
    @Value("#{target.userSend}")
    UserPay getSendId();
    
    //Id người nhận
    @Value("#{target.userReceived}")
    UserPay getReceivedId();
    
    //Nội dung thanh toán
    StoryPay getStory();
    
    ChapterPay getChapter();
    
    //Số tiền giao dịch
    Double getMoney();
    
    //Ngày giao dịch
    Date getCreateDate();
    
    //Loại giao dịch
    Integer getType();
    
    //Số phiếu đề cử
    Integer getVote();
    
    //Trạng Thái Giao dịch
    Integer getStatus();
    
    interface UserPay {
        Long getId();
    }
    
    interface StoryPay {
        Long getId();
        
        String getVnName();
    }
    
    interface ChapterPay {
        Long getId();
        
        String getChapterNumber();
        
        StoryPay getStory();
    }
}
