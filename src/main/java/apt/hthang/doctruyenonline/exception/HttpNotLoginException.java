package apt.hthang.doctruyenonline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Đời Không Như Là Mơ on 28/11/2018
 * @project truyenonline
 */

@ResponseStatus(HttpStatus.FORBIDDEN)
public class HttpNotLoginException extends Exception {
    private static final String EXCEPTION_MESSAGE = "Bạn cần Đăng Nhập trước khi thực hiện hành động này";
    public HttpNotLoginException() {
        super(EXCEPTION_MESSAGE);
    }

    public HttpNotLoginException(String messeger) {
        super(messeger);
    }
}
