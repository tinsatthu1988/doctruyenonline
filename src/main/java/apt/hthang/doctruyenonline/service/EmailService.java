package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Mail;

/**
 * @author Huy Thang
 */
public interface EmailService {

    boolean sendSimpleMessage(Mail mail);

}
