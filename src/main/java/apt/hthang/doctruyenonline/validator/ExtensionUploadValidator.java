package apt.hthang.doctruyenonline.validator;

import apt.hthang.doctruyenonline.annotations.ExtensionUpload;
import apt.hthang.doctruyenonline.utils.WebUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Đời Không Như Là Mơ on 13/10/2018
 * @project truyenmvc
 */

public class ExtensionUploadValidator implements ConstraintValidator< ExtensionUpload, MultipartFile > {
    
    private String message;
    
    @Override
    public void initialize(ExtensionUpload constraintAnnotation) {
        message = constraintAnnotation.message();
    }
    
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        try {
            String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            if (!multipartFile.isEmpty() && fileExtension != null) {
                if (WebUtils.checkExtension(fileExtension)) {
                    valid = false;
                    message = "Chỉ upload ảnh có định dạng JPG | JPEG | PNG!";
                } else {
                    if (multipartFile.getSize() > (20 * 1024 * 1024)) {
                        valid = false;
                        message = "Kích thước ảnh upload tối đa là 20 Megabybtes!";
                    }
                }
            }
            
        } catch (Exception ignored) {
        
        }
        if (!valid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
