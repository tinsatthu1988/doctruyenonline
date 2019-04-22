package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.entity.UserRating;
import apt.hthang.doctruyenonline.exception.ExceptionResponse;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.projections.StoryMember;
import apt.hthang.doctruyenonline.projections.StorySlide;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserRatingService;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Huy Thang
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
    
}
