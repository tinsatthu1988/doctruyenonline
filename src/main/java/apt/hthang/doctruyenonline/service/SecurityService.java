package apt.hthang.doctruyenonline.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Đời Không Như Là Mơ on 15/10/2018
 * @project truyenmvc
 */
public interface SecurityService {
    
    /**
     * Tự động đăng nhập sau khi đăng ký thành công
     *
     * @param username
     * @param password
     * @param request
     * @return void
     */
    void autologin(String username, String password, HttpServletRequest request);
}
