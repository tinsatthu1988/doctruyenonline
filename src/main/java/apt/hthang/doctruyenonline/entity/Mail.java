package apt.hthang.doctruyenonline.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Đời Không Như Là Mơ
 */

@NoArgsConstructor
@Data
public class Mail {

    private String from;
    private String fromDisplay;
    private String to;
    private String subject;
    private Map<String, Object> model;


}
