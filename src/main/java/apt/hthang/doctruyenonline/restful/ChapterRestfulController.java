package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.projections.ChapterOfStory;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/chapter")
public class ChapterRestfulController {
    
    @Autowired
    private ChapterService chapterService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PayService payService;
    
    @PostMapping(value = "/chapterOfStory")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("type") Integer type) {
        Page< ChapterOfStory > chapterOfStoryPage = chapterService
                .getListChapterOfStory(storyId, pagenumber, ConstantsListUtils.LIST_CHAPTER_DISPLAY, type);
        return new ResponseEntity<>(chapterOfStoryPage, HttpStatus.OK);
    }
    
    @PostMapping(value = "/pay")
    @Transactional
    public ResponseEntity< ? > payChapterVip(@RequestParam(value = "chapterId") String chapterId, Principal principal)
            throws Exception {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            throw new HttpNotLoginException("Tài khoản không tồn tại");
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            throw new HttpUserLockedException();
        }
        if (chapterId == null || WebUtils.checkLongNumber(chapterId)) {
            throw new HttpMyException("Có lỗi xảy ra! Mong bạn quay lại sau.");
        }
        Chapter chapter = chapterService
                .findChapterByIdAndStatus(Long.valueOf(chapterId), ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        if (chapter == null) {
            throw new HttpMyException("Không tồn tại chương truyện này!");
        }
        if (chapter.getStatus() == 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();
        
        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);
        if (payService.checkDealChapterVip(Long.valueOf(chapterId), user.getId(), dayAgo, now)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        if (user.getGold() < chapter.getPrice()) {
            throw new HttpMyException("Số dư trong tài khoản không đủ để thanh toán!");
        }
        boolean payCheck = payService
                .savePay(null, chapter, user, chapter.getUser(), chapter.getPrice(), ConstantsPayTypeUtils.PAY_CHAPTER_VIP_TYPE);
        if (payCheck) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new HttpMyException("Có lỗi xảy ra. Vui lòng thử lại sau");
        }
    }
    
}
