package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.PayService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsPayTypeUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/appoint")
public class AppointRestfulController {
    
    private final UserService userService;
    private final PayService payService;
    private final StoryService storyService;
    
    public AppointRestfulController(UserService userService, PayService payService, StoryService storyService) {
        this.userService = userService;
        this.payService = payService;
        this.storyService = storyService;
    }
    
    @PostMapping(value = "/save")
    public ResponseEntity< ? > appoindStory(@RequestParam("storyId") Long storyId,
                                            @RequestParam("coupon") Integer coupon,
                                            Principal principal) throws Exception {
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
        Story story = storyService.findStoryByIdAndStatus(storyId, ConstantsListUtils.LIST_STORY_DISPLAY);
        if (story == null) {
            throw new HttpMyException("Truyện không tồn tại hoặc đã bị xóa!");
        }
        if (coupon <= 0) {
            throw new HttpMyException("Số phiếu đề cử ít nhất là 1!");
        }
        if (user.getGold() < (coupon * 1000))
            throw new HttpMyException("Số dư của bạn không đủ để đề cử");
        boolean check = payService.savePay(story, null, user, null,coupon,
                (double) (coupon * 1000), ConstantsPayTypeUtils.PAY_APPOINT_TYPE);
        if (check)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            throw new HttpMyException("Có lỗi xảy ra mong bạn quay lại sau!");
    }
}
