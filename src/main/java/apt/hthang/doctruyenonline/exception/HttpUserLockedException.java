package apt.hthang.doctruyenonline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Đời Không Như Là Mơ on 28/11/2018
 * @project truyenonline
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HttpUserLockedException extends  Exception{
    private static final String EXCEPTION_MESSAGE = "Tài Khoản đã bị khóa. Liên hệ với admin để biết thêm chi tiết";
    public HttpUserLockedException() {
        super(EXCEPTION_MESSAGE);
    }
}
