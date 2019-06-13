package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.entity.UserFollow;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.StoryService;
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
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/follow")
public class FollowRestfulController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private StoryService storyService;
    
    @PostMapping(value = "/followOfUser")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("pagenumber") Integer pagenumber, Principal principal)
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
        return new ResponseEntity<>(userFollowService.findAllStoryFollowByUserId(user.getId(), pagenumber,
                ConstantsUtils.PAGE_SIZE_DEFAULT), HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/cancel_follow/{storyId}/{userId}")
    public ResponseEntity< ? > deletePayDraw(@PathVariable("storyId") Long storyId, @PathVariable("userId") Long userId,
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
    
    @PostMapping(value = "/add")
    public ResponseEntity< ? > loadChapterOfStory(@RequestParam("storyId") Long storyId, Principal principal)
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
        Story story = storyService.findStoryById(storyId);
        if (story == null)
            throw new HttpMyException("Truyện Không Tồn Tại hoặc Đã Bị Xóa");
        UserFollow userFollow = userFollowService.findByUserIdAndStoryId(user.getId(), storyId);
        if (userFollow != null)
            throw new HttpNotLoginException("Bạn đã theo dõi truyện!");
        UserFollow newUserFollow = new UserFollow();
        newUserFollow.setStory(story);
        newUserFollow.setUser(user);
        userFollowService.saveFollow(newUserFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/delete")
    public ResponseEntity< ? > removeChapterOfStory(@RequestParam("storyId") Long storyId, Principal principal)
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
        UserFollow userFollow = userFollowService.findByUserIdAndStoryId(user.getId(), storyId);
        if (userFollow == null)
            throw new HttpNotLoginException("Bạn không theo dõi truyện!");
        userFollowService.deleteFollow(userFollow);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping(value = "/checkFollow")
    public ResponseEntity< ? > checkFollowStoryOfUser(Principal principal, @RequestParam("storyId") Long storyId)
            throws Exception {
        if (principal == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        MyUserDetails myUser = (MyUserDetails) ((Authentication) principal).getPrincipal();
        User user = myUser.getUser();
        user = userService.findUserById(user.getId());
        if (user == null) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        if (user.getStatus().equals(ConstantsStatusUtils.USER_DENIED)) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(userFollowService.existsUserFollow(user.getId(), storyId), HttpStatus.OK);
    }
}
