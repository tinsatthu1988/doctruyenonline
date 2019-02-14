package apt.hthang.doctruyenonline.service.impl;

import apt.hthang.doctruyenonline.entity.MyUserDetails;
import apt.hthang.doctruyenonline.entity.Role;
import apt.hthang.doctruyenonline.entity.User;
import apt.hthang.doctruyenonline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = this.userService.findUserAccount(userName);

        if (user == null) {
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        List< GrantedAuthority > grantList = new ArrayList< GrantedAuthority >();
        if (user.getRoleList() != null) {
            for (Role urole : user.getRoleList()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(urole.getName());
                grantList.add(authority);
            }
        }

        return new MyUserDetails(user, grantList);
    }

}