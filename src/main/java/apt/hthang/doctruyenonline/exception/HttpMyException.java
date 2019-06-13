package apt.hthang.doctruyenonline.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Đời Không Như Là Mơ on 02/12/2018
 * @project truyenonline
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HttpMyException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Bạn cần Đăng Nhập trước khi thực hiện hành động này";

    public HttpMyException() {
        super(EXCEPTION_MESSAGE);
    }

    public HttpMyException(String messeger) {
        super(messeger);
    }
}
