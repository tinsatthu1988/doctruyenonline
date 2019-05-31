package apt.hthang.doctruyenonline.configuration;

import apt.hthang.doctruyenonline.service.impl.UserDetailsServiceImpl;
import apt.hthang.doctruyenonline.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    private final UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Sét đặt dịch vụ để tìm kiếm User trong Database.
        // Và sét đặt PasswordEncoder.
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.csrf().disable();
        
        // Các trang không yêu cầu login
        http.authorizeRequests()
                .antMatchers(SecurityUtils.PERMIT_ALL_LINK).permitAll();
        
        // Trang /userInfo yêu cầu phải login với tối thiểu là vai trò ROLE_USER
        // Nếu chưa login, nó sẽ redirect tới trang /login.
        http.authorizeRequests().antMatchers(SecurityUtils.ROLE_USER_LINK).access("hasAnyRole('ROLE_USER')");
        
        // Trang /userInfo yêu cầu phải login với tối thiểu là vai trò ROLE_USER
        // Nếu chưa login, nó sẽ redirect tới trang /login.
        http.authorizeRequests().antMatchers(SecurityUtils.ROLE_CONANDMOD_LINK).access("hasAnyRole('ROLE_CONVERTER', 'ROLE_SMOD')");
        // Trang chỉ dành cho ADMIN
        http.authorizeRequests().antMatchers(SecurityUtils.ROLE_ADMIN_MOD_LINK).access("hasAnyRole('ROLE_ADMIN', 'ROLE_SMOD')");
        
        // Khi người dùng đã login, với vai trò XX.
        // Nhưng truy cập vào trang yêu cầu vai trò YY,
        // Ngoại lệ AccessDeniedException sẽ ném ra.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403error");
        
        // Cấu hình cho Login Form.
        http.authorizeRequests()
                .and()
                .formLogin()//
                // Submit URL của trang login
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/dang-nhap")//
                .usernameParameter("username")//
                .passwordParameter("password")
                .defaultSuccessUrl("/")//
                .failureUrl("/dang-nhap?error")
                // Cấu hình cho Logout Page.
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
        
        // Cấu hình Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(86400); // 24h
        
    }
    
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/mdbootstrap/**");
    }
    
}