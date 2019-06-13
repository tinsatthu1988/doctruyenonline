package apt.hthang.doctruyenonline.service;

import apt.hthang.doctruyenonline.entity.Information;

/**
 * @author Đời Không Như Là Mơ
 */
public interface InformationService {

    /**
     * Lấy Thông Tin Web
     *
     * @return Information- Nếu tồn tại Information/ null - nếu không tồn tại Information
     */
    Information getWebInfomation();
}
