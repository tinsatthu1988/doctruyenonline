package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.entity.*;
import apt.hthang.doctruyenonline.exception.HttpMyException;
import apt.hthang.doctruyenonline.exception.HttpNotLoginException;
import apt.hthang.doctruyenonline.exception.HttpUserLockedException;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.service.ReportService;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.ConstantsStatusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/reprot")
public class ReportRestfulController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ReportService reportService;
    
    @PostMapping(value = "/add")
    public ResponseEntity< ? > submitPayDraw(@RequestParam("chapter") Long chapterId, @RequestParam("content") String content,
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
        Chapter chapter = chapterService.findChapterById(chapterId);
        Report report =  reportService.saveReport(chapter, user, content);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
