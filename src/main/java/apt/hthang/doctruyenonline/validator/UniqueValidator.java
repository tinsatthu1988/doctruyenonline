package apt.hthang.doctruyenonline.validator;


import apt.hthang.doctruyenonline.annotations.Unique;
import apt.hthang.doctruyenonline.service.FieldValueExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Đời Không Như Là Mơ
 * @project doctruyenonline
 */

public class UniqueValidator implements ConstraintValidator< Unique, Object > {
    @Autowired
    private ApplicationContext applicationContext;
    
    private FieldValueExists service;
    private String fieldName;
    private String message;
    
    @Override
    public void initialize(Unique unique) {
        Class< ? extends FieldValueExists > clazz = unique.service();
        this.fieldName = unique.fieldName();
        String serviceQualifier = unique.serviceQualifier();
        
        if (!serviceQualifier.equals("")) {
            this.service = this.applicationContext.getBean(serviceQualifier, clazz);
        } else {
            this.service = this.applicationContext.getBean(clazz);
        }
        message = unique.message();
    }
    
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = !this.service.fieldValueExists(o, this.fieldName);
        if (valid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
    
}