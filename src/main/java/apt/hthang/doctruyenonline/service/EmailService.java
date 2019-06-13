package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Mail;

/**
 * @author Đời Không Như Là Mơ
 */
public interface EmailService {
    
    boolean sendSimpleMessage(Mail mail, String template);
    
}
