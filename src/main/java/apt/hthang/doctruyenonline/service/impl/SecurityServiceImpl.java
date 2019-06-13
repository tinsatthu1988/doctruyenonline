package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Đời Không Như Là Mơ on 15/10/2018
 * @project truyenmvc
 */

@Service
public class SecurityServiceImpl implements SecurityService {
    
    private final AuthenticationManager authenticationManager;
    
    private final UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    public SecurityServiceImpl(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * Tự động đăng nhập sau khi đăng ký thành công
     *
     * @param username
     * @param password
     * @param request
     * @return void
     */
    @Override
    public void autologin(String username, String password, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                password, userDetails.getAuthorities());
        
        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        
        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
        }
    }
}
