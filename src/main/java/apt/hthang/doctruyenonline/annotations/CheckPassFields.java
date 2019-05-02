package apt.hthang.doctruyenonline.annotations;

import apt.hthang.doctruyenonline.validator.CheckPassFieldsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckPassFieldsValidator.class)
@Documented
public @interface CheckPassFields {
    
    String message() default "";
    
    Class< ? >[] groups() default {};
    
    Class< ? extends Payload >[] payload() default {};
    
    String baseField();
    
    String matchField();
    
}
