package apt.hthang.doctruyenonline.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author Đời Không Như Là Mơ on 16/10/2018
 * @project doctruyenonline
 */

@NoArgsConstructor
@Data
public class ForgotUser implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @NotEmpty(message = "{hthang.truyenonline.forgotuser.username.empty.message}")
    private String username;
    
    @NotEmpty(message = "{hthang.truyenonline.forgotuser.email.empty.message}")
    private String email;
}
