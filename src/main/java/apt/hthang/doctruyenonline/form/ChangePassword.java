package apt.hthang.doctruyenonline.form;

import apt.hthang.doctruyenonline.annotations.EqualFields;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */
@NoArgsConstructor
@Data
@EqualFields(baseField = "newPassword", matchField = "confirmNewPassword",
        message = "{hthang.truyenonline.reset.conNewPass.EqualFields.message}")
public class ChangePassword {
    
    @NotEmpty(message = "{hthang.truyenonline.reset.oldPass.empty.message}")
    private String oldPassword;
    @Size(min = 6, max = 13, message = "{hthang.truyenonline.reset.newPass.size.message}")
    private String newPassword;
    @NotEmpty(message = "{hthang.truyenonline.reset.conNewPass.empty.message}")
    private String confirmNewPassword;
}
