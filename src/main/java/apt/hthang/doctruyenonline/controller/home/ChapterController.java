package apt.hthang.doctruyenonline.controller.home;

import apt.hthang.doctruyenonline.entity.Chapter;
import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.NotFoundException;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */

@Controller
@RequestMapping("/truyen")
public class ChapterController {
    
    Logger logger = LoggerFactory.getLogger(ChapterController.class);
    @Autowired
    private InformationService informationService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private PayService payService;
    @Autowired
    private HistoryService historyService;
    
    private void getMenuAndInfo(Model model, String title) {
        
        // Lấy Title Cho Page
        model.addAttribute("title", title);
        
        // Lấy List Category cho Menu
        model.addAttribute("categorylist", categoryService.getListCategoryOfMenu(ConstantsStatusUtils.CATEGORY_ACTIVED));
        
        // Lấy Information của Web
        model.addAttribute("information", informationService.getWebInfomation());
    }
    
    @RequestMapping("/{sID}/chuong-{chID}")
    public String chapterPage(@PathVariable("sID") String sid,
                              @PathVariable("chID") String chid,
                              Principal principal,
                              Model model,
                              HttpServletRequest request) throws Exception {
        
        //Kiểm Tra Type sID và chID
        //Nếu Không Chuyển Trang Lỗi
        if (sid == null || WebUtils.checkLongNumber(sid)
                || chid == null || WebUtils.checkLongNumber(chid)) {
            throw new NotFoundException();
        }
        Long sID = Long.parseLong(sid);
        Long chID = Long.parseLong(chid);
        
        //Lấy Chapter Theo sID và chID
        Chapter chapter = chapterService.findChapterByStoryIdAndChapterID(sID, ConstantsListUtils.LIST_STORY_DISPLAY,
                chID, ConstantsListUtils.LIST_CHAPTER_DISPLAY);
        
        
        String title = chapter.getStory().getVnName()
                + " - Chương " + chapter.getChapterNumber()
                + ": " + chapter.getName();
        
        //Lấy Thời Gian Hiện Tại
        Date now = DateUtils.getCurrentDate();
        
        // Lấy Thời Gian 24h Trước
        Date dayAgo = DateUtils.getHoursAgo(now, ConstantsUtils.TIME_DAY);
        
        // Lấy thời gian 30 phút trước
        Date halfHourAgo = DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR);
        
        User user = checkUserLogin(principal);
        
        //Lấy LocationIP Client
        String locationIP = getLocationIP(request);
        
        getMenuAndInfo(model, title);
        
        checkVipStory(model, user, chapter, dayAgo, now);
        
        //Lấy Chapter ID Next Và Previous
        getPreAndNextChapter(model, chapter);
        
        model.addAttribute("chapter", chapter);
        
        //Lưu Lịch Sử Đọc Truyện
        saveFavorites(user, chapter, halfHourAgo, now, locationIP);
        if (chapter.getUser().getAvatar() == null || chapter.getUser().getAvatar().isEmpty()) {
            chapter.getUser().setAvatar(ConstantsUtils.AVATAR_DEFAULT);
        }
        return "view/chapterPage";
    }
    
    // Kiểm Tra Người Dùng Đã Đăng Nhập Chưa
    private User checkUserLogin(Principal principal) {
        User user = null;
        if (principal != null) {
            
            // Lấy Người Dùng đang đăng nhập
            MyUserDetails loginedUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
            user = loginedUser.getUser();
        }
        return user;
    }
    
    private void checkVipStory(Model model,
                               User user,
                               Chapter chapter,
                               Date dayAgo,
                               Date now) {
        boolean check = true;
        
        //Kiểm Tra Chapter có phải tính phí hay không
        //Chapter tính phí là chapter có chStatus = 2
        if (chapter.getStatus() == 2) {
            
            // Kiểm tra người dùng đã đăng nhập chưa
            if (user != null) {
                //Kiểm tra người dùng có phải người đăng chapter không
                boolean checkUser = user.equals(chapter.getUser());
                // Kiểm tra người dùng đã thanh toán chương vip trong 24h qua không
                // Nếu chưa thanh toán rồi thì check = false
                if (!checkUser) {
                    boolean checkPay = checkDealStory(chapter.getId(), user.getId(), dayAgo, now);
                    if (!checkPay) {
                        check = false;
                    }
                }
            } else {
                check = false;
            }
        }
        
        model.addAttribute("checkVip", check);
    }
    
    private boolean checkDealStory(Long chID,
                                   Long uID,
                                   Date dayAgo,
                                   Date now) {
        boolean check = payService
                .checkDealChapterVip(chID, uID, dayAgo, now);
        return check;
    }
    
    //Lấy Địa Chỉ Ip client
    private String getLocationIP(HttpServletRequest request) {
        String remoteAddr = "";
        
        //Kiểm Tra HttpServletRequest có null
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        
        return remoteAddr;
    }
    
    private void saveFavorites(User user,
                               Chapter chapter,
                               Date halfHourAgo,
                               Date now,
                               String LocationIP) throws Exception {
        // Kiểm Tra đã đọc Chapter trong Khoảng
        boolean check = historyService
                .checkChapterAndLocationIPInTime(chapter.getId(), LocationIP, halfHourAgo, now);
        int uView = 1;
        
        if (check) {
            uView = 0;
        } else {
            // Chưa Đọc Chapter Trong Khoảng 30 phút Thì Tăng Lượt View Của Chapter
            chapterService.updateViewChapter(chapter);
        }
        historyService.saveHistory(chapter, user, LocationIP, uView);
    }
    
    private void getPreAndNextChapter(Model model,
                                      Chapter chapter) {
        model.addAttribute("preChapter", chapterService
                .findPreviousChapterID(chapter.getSerial(), chapter.getStory().getId()));
        
        model.addAttribute("nextChapter", chapterService.
                findNextChapterID(chapter.getSerial(), chapter.getStory().getId()));
    }
}
