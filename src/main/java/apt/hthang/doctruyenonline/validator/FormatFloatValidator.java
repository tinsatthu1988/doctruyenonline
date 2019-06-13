package apt.hthang.doctruyenonline.validator;

import apt.hthang.doctruyenonline.annotations.FormatFloat;
import apt.hthang.doctruyenonline.entity.Story;
import apt.hthang.doctruyenonline.service.ChapterService;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author Đời Không Như Là Mơ on 13/10/2018
 * @project truyenmvc
 */

public class FormatFloatValidator implements ConstraintValidator< FormatFloat, Object > {
    
    Logger logger = LoggerFactory.getLogger(FormatFloatValidator.class);
    @Autowired
    private ChapterService chapterService;
    private String message;
    private String baseField;
    private String matchField;
    private String idFielad;
    
    @Override
    public void initialize(FormatFloat constraint) {
        baseField = constraint.baseField();
        matchField = constraint.matchField();
        idFielad = constraint.idField();
        message = constraint.message();
    }
    
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            Story baseFieldValue = (Story) getFieldValue(object, baseField);
            Object matchFieldValue = getFieldValue(object, matchField);
            Object idFieldValue = getFieldValue(object, idFielad);
            if (matchFieldValue != null) {
                if (WebUtils.checkFloatNumber(matchFieldValue.toString())) {
                    valid = false;
                    message = "Số thứ tự phải là số";
                } else {
                    float number = Float.parseFloat(matchFieldValue.toString());
                    if (idFieldValue != null) {
                        long chapterId = Long.parseLong(idFieldValue.toString());
                        if (chapterService.checkChapterBySerialAndId(chapterId, baseFieldValue.getId(), number)) {
                            valid = false;
                            message = "Đã tồn tại số thứ tự này!";
                        }
                    } else {
                        if (chapterService.checkChapterBySerial(baseFieldValue.getId(), number)) {
                            valid = false;
                            message = "Đã tồn tại số thứ tự này!";
                        }
                    }
                }
            } else {
                valid = false;
                message = "Số thứ tự Chương không được để trống";
            }
        } catch (Exception ignored) {
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
