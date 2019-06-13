package apt.hthang.doctruyenonline.annotations;

import apt.hthang.doctruyenonline.service.FieldValueExists;
import apt.hthang.doctruyenonline.validator.UniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface Unique {
    
    String message() default "";
    
    Class< ? >[] groups() default {};
    
    Class< ? extends Payload >[] payload() default {};
    
    Class< ? extends FieldValueExists > service();
    
    String serviceQualifier() default "";
    
    String fieldName();
    
}