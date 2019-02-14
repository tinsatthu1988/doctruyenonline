package apt.hthang.doctruyenonline.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Huy Thang
 * @project doctruyenonline
 */
@Configuration
@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE - 90)
public class WebSercurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public WebSercurityConfiguration() {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable();

        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests()
                .and()
                .formLogin()
                .loginPage("/dang_nhap")
                .loginProcessingUrl("/j_spring_security_login")
                .defaultSuccessUrl("/")
                .failureUrl("/dang_nhap?message=error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .exceptionHandling().accessDeniedPage("/403");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
}
