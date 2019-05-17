package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.entity.UserFollow;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.UserFollowService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import apt.hthang.doctruyenonline.utils.ConstantsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/follow")
public class FollowRestfulController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;
    
    @PostMapping(value = "/followOfUser")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("pagenumber") Integer pagenumber,
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
        return new ResponseEntity<>(userFollowService
                .findAllStoryFollowByUserId(user.getId(), pagenumber, ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/cancel_follow/{storyId}/{userId}")
    public ResponseEntity< ? > deletePayDraw(@PathVariable("storyId") Long storyId,
                                             @PathVariable("userId") Long userId,
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
        if (user.getId() != userId)
            throw new HttpNotLoginException("Bạn không có quyền hủy theo dõi truyện không phải của bản thân!");
        UserFollow userFollow = userFollowService.findByUserIdAndStoryId(userId, storyId);
        if (userFollow == null)
            throw new HttpNotLoginException("Bạn không theo dõi truyện!");
        userFollowService.deleteFollow(userFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
