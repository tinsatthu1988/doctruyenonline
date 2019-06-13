package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.entity.UserRating;
import apt.hthang.doctruyenonline.exception.ExceptionResponse;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.projections.*;
import apt.hthang.doctruyenonline.service.*;
import apt.hthang.doctruyenonline.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/story")
public class StoryRestfulController {
    
    @Autowired
    private StoryService storyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRatingService userRatingService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private PayService payService;
    
    //Lấy Top 3 Truyện mới đăng của Converter
    @PostMapping(value = "/storyOfConverter")
    public ResponseEntity< ? > loadStoryOfConverter(@RequestParam("userId") Long userId) {
        List< StorySlide > list = storyService
                .findStoryOfConverter(userId, ConstantsListUtils.LIST_STORY_DISPLAY);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    
    @PostMapping(value = "/storyOfMember")
    public ResponseEntity< ? > loadStoryOfMember(@RequestParam("userId") Long userId,
                                                 @RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("type") int type) {
        Page< StoryMember > storyMembers = storyService
                .findStoryByUserId(userId, ConstantsListUtils.LIST_STORY_DISPLAY,
                        pagenumber, type, ConstantsUtils.PAGE_SIZE_DEFAULT);
        return new ResponseEntity<>(storyMembers, HttpStatus.OK);
    }
    
    @PostMapping(value = "/rating")
    @Transactional
    public ResponseEntity< Object > saveUserAvatar(@RequestParam("idBox") Long idBox,
                                                   @RequestParam("rate") Integer rate,
                                                   HttpServletRequest request,
                                                   Principal principal) throws Exception {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setError(false);
        exceptionResponse.setServer("");
        if (principal == null) {
            exceptionResponse.setMessage("Bạn chưa đăng nhập!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            exceptionResponse.setMessage("Tài khoản không tồn tại!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            exceptionResponse.setMessage("Tài khoản của bạn đã bị khóa. Mời liên hệ admin!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        Story story = storyService.findStoryByIdAndStatus(idBox, ConstantsListUtils.LIST_STORY_DISPLAY);
        if (story == null) {
            exceptionResponse.setMessage("Truyện không tồn tại hoặc đã bị xóa!");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        if (userRatingService.existsRatingWithUser(idBox, user.getId())) {
            exceptionResponse.setMessage("Bạn đã đánh giá truyện này rồi");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        String locationIP = WebUtils.getLocationIP(request);
        Date now = DateUtils.getCurrentDate();
        UserRating userRating = userRatingService.existsRatingWithLocationIP(idBox, locationIP,
                DateUtils.getMinutesAgo(now, ConstantsUtils.HALF_HOUR), now);
        if (userRating != null) {
            exceptionResponse.setMessage("Đã có đánh giá truyện tại địa chỉ IP này. Hãy đợi " + DateUtils.betweenHours(userRating.getCreateDate()) + " để tiếp tục đánh giá");
            return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
        }
        Float result = userRatingService.saveRating(user.getId(), idBox, locationIP, rate);
        //Lưu đánh giá
        if (result != -1) {
            exceptionResponse.setMyrating(userRatingService.countRatingStory(idBox));
            DecimalFormat df = new DecimalFormat("#.0");
            exceptionResponse.setMyrate(df.format(result));
        }
        return new ResponseEntity<>(exceptionResponse, HttpStatus.OK);
    }
    
    @PostMapping(value = "/search")
    public ResponseEntity< ? > searchStory(@RequestParam("txtSearch") String txtSearch) {
        String decryptedText = new String(java.util.Base64.getDecoder().decode(txtSearch));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String searchText = aesUtil.decrypt(decryptedText.split("::")[1], decryptedText.split("::")[0], "1234567891234567", decryptedText.split("::")[2]);
            return new ResponseEntity<>(storyService
                    .findListStoryBySearchKey(searchText, ConstantsListUtils.LIST_STORY_DISPLAY), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
    
    @PostMapping(value = "/list_story")
    public ResponseEntity< ? > getStoryByAccount(@RequestParam("pagenumber") int pagenumber,
                                                 @RequestParam("status") int status,
                                                 Principal principal)
            throws HttpNotLoginException {
        if (principal == null) {
            throw new HttpNotLoginException();
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        Page< StoryUser > pageStory = storyService.findPageStoryByUser(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT, status);
        return new ResponseEntity<>(pageStory, HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/delete_story/{id}")
    public ResponseEntity< ? > deleteStory(@PathVariable("id") Long id, Principal principal) throws Exception {
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
        Story story = storyService.findStoryById(id);
        if (story == null)
            throw new HttpMyException("Truyện Không Tồn Tại hoặc Đã Bị Xóa");
        if (!story.getUser().getId().equals(user.getId()))
            throw new HttpMyException("Bạn không có quyền Xóa Truyện Không phải bản thân đăng!");
        Long countChapter = chapterService.countChapterByStory(story.getId());
        Long countPay = payService.countPayOfStory(id);
        Long countRating = userRatingService.countRatingStory(id);
        if (countChapter > 0 || countPay > 0 || countRating > 0
                || story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_COMPLETED)
                || story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_GOING_ON))
            throw new HttpMyException("Không thể xóa truyện!");
        storyService.deleteStoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    //Lấy Top 3 Truyện Mới Của Converter
    @PostMapping(value = "/topAppoidMonth")
    public ResponseEntity< ? > loadStoryTopViewMonth() {
        Date startDate = DateUtils.getFirstDayOfMonth();
        Date endDate = DateUtils.getLastDayOfMonth();
        // Lấy Danh Sách Truyện Top View trong tháng
        List< StoryTop > topstory = storyService
                .getTopStoryAppoind(ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.RANK_SIZE, startDate, endDate)
                .getContent();
        return new ResponseEntity<>(topstory, HttpStatus.OK);
    }
    
    //Lấy Top 3 Truyện Mới Của Converter
    @PostMapping(value = "/storyNewUpdate")
    public ResponseEntity< ? > loadStoryNewUpdate() {
        // Lấy Danh Sách Truyện Mới Cập Nhật
        List< StoryUpdate > listNewStory = storyService
                .findStoryUpdateByStatus(ConstantsListUtils.LIST_CHAPTER_DISPLAY, ConstantsListUtils.LIST_STORY_DISPLAY,
                        ConstantsUtils.PAGE_DEFAULT, ConstantsUtils.PAGE_SIZE_HOME)
                .getContent();
        return new ResponseEntity<>(listNewStory, HttpStatus.OK);
    }
    
    
    @DeleteMapping(value = "/admin/delete/{id}")
    public ResponseEntity< ? > deleteStoryAdmin(@PathVariable("id") Long id, Principal principal) throws Exception {
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
        Story story = storyService.findStoryById(id);
        if (story == null)
            throw new HttpMyException("Truyện Không Tồn Tại hoặc Đã Bị Xóa");
        Long countChapter = chapterService.countChapterByStory(story.getId());
        Long countPay = payService.countPayOfStory(id);
        Long countRating = userRatingService.countRatingStory(id);
        if (countChapter > 0 || countPay > 0 || countRating > 0
                || story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_COMPLETED)
                || story.getStatus().equals(ConstantsStatusUtils.STORY_STATUS_GOING_ON))
            throw new HttpMyException("Không thể xóa truyện!");
        storyService.deleteStoryById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/admin/listStory")
    public ResponseEntity< ? > loadStoryAdmin(@RequestParam("pagenumber") Integer pagenumber, @RequestParam("search") String search,
                                              @RequestParam("type") Integer type) {
        return new ResponseEntity<>(storyService.findStoryInAdmin(pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT, type, search), HttpStatus.OK);
    }

}
