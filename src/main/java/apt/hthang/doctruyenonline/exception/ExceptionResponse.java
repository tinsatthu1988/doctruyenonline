package apt.hthang.doctruyenonline.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    Boolean error;
    String message;
    String server;
    String myrate;
    Long myrating;
    
}
