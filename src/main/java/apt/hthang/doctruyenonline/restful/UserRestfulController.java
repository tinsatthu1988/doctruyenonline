package apt.hthang.doctruyenonline.restful;

import apt.hthang.doctruyenonline.projections.ConveterSummary;
import apt.hthang.doctruyenonline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserRestfulController {
    
    private final UserService userService;
    
    public UserRestfulController(UserService userService) {
        this.userService = userService;
    }
    
    //Lấy Thông Tin Converter
    @PostMapping(value = "/converterInfo")
    public ResponseEntity< ? > loadConverter(@RequestParam("userId") Long userId) {
        ConveterSummary conveterSummary = userService.findConverterByID(userId);
        return new ResponseEntity<>(conveterSummary, HttpStatus.OK);
    }
}
