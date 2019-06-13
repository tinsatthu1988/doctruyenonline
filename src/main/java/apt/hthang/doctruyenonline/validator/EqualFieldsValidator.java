package apt.hthang.doctruyenonline.validator;


import apt.hthang.doctruyenonline.annotations.EqualFields;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author Đời Không Như Là Mơ on 14/10/2018
 * @project truyenmvc
 */

public class EqualFieldsValidator implements ConstraintValidator< EqualFields, Object> {

    private String baseField;
    private String matchField;
    private String message;

    @Override
    public void initialize(EqualFields constraint) {
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
            valid = baseFieldValue == null && matchFieldValue == null
                    || baseFieldValue != null && baseFieldValue.equals(matchFieldValue);
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
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }

}