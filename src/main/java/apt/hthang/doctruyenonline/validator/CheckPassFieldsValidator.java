package apt.hthang.doctruyenonline.validator;


import apt.hthang.doctruyenonline.annotations.CheckPassFields;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.service.UserService;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author Huy Thang on 14/10/2018
 * @project truyenmvc
 */

public class CheckPassFieldsValidator implements ConstraintValidator< CheckPassFields, Object > {
    
    private String baseField;
    private String matchField;
    private String message;
    
    @Autowired
    private UserService userService;
    
    @Override
    public void initialize(CheckPassFields constraint) {
        baseField = constraint.baseField();
        matchField = constraint.matchField();
        message = constraint.message();
    }
    
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            Object baseFieldValue = getFieldValue(object, baseField);
            Object matchFieldValue = getFieldValue(object, matchField);
            if (matchFieldValue != null && !((String) matchFieldValue).trim().isEmpty()) {
                User user = userService.findUserById((Long) baseFieldValue);
                valid = user.getPassword().equals(WebUtils.encrypString((String) matchFieldValue));
            }
        } catch (Exception e) {
        }
        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(matchField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        
        return valid;
    }
    
    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class< ? > clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
    
}