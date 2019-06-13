package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.projections.CommentSummary;
import apt.hthang.doctruyenonline.service.CommentService;
import apt.hthang.doctruyenonline.service.StoryService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.AesUtil;
import apt.hthang.doctruyenonline.utils.ConstantsListUtils;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping(value = "/api/comment")
public class CommentRestfulController {
    
    private final CommentService commentService;
    
    private final UserService userService;
    
    private final StoryService storyService;
    
    @Autowired
    public CommentRestfulController(CommentService commentService, UserService userService, StoryService storyService) {
        this.commentService = commentService;
        this.userService = userService;
        this.storyService = storyService;
    }
    
    @PostMapping(value = "/load")
    public ResponseEntity< ? > loadCommentOfStory(@RequestParam("storyId") Long storyId,
                                                  @RequestParam("pagenumber") Integer pagenumber,
                                                  @RequestParam("type") Integer type) {
        Page< CommentSummary > commentSummaryPage = commentService.getListCommentOfStory(storyId, pagenumber, type);
        return new ResponseEntity<>(commentSummaryPage, HttpStatus.OK);
    }
    
    @PostMapping(value = "/add")
    public ResponseEntity< ? > newComment(@RequestParam("storyId") Long storyId,
                                          @RequestParam("commentText") String commentTextEncode,
                                          Principal principal) throws Exception {
        String decryptedText = new String(java.util.Base64.getDecoder().decode(commentTextEncode));
        AesUtil aesUtil = new AesUtil(128, 1000);
        if (decryptedText.split("::").length == 3) {
            String commentText = aesUtil.decrypt(decryptedText.split("::")[1],
                    decryptedText.split("::")[0],
                    "1234567891234567",
                    decryptedText.split("::")[2]);
            if (commentText.trim().length() == 0) {
                throw new HttpMyException("Bình luận không được để trống");
            }
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
            boolean check = commentService.saveComment(user, story, commentText.replaceAll("\n", "<br />"));
            if (check) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new HttpMyException("Có lỗi xảy ra. Mong bạn quay lại sau");
            }
        } else {
            throw new HttpMyException("Có lỗi xảy ra. Mong bạn quay lại sau");
        }
    }
    
}
