package org.spring.compose.auth.service;

import lombok.RequiredArgsConstructor;
import org.spring.compose.auth.model.SysUserDetails;
import org.spring.compose.common.model.user.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    /**
     * 根据用户名获取用户信息(用户名、密码和角色权限)
     * <p>
     * 用户名、密码用于后续认证，认证成功之后将权限授予用户
     *
     * @param username 用户名
     * @return {@link  org.spring.compose.auth.model.SysUserDetails}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = AuthUser.builder()
                .id(10066993L)
                .username("10066993")
                .password("$2a$10$qkB3g30IsUVECKXuIEY7MeLbRr2vhlWgvK.hgUo0zBOhvGUQ.Kp4m")
                .gender("man")
                .nickname("皮皮虎的主人")
                .avatar("")
                .mobile("13007350735")
                .email("")
                .name("")
                .dataScope(1)
                .roles(Set.of("admin", "normal"))
                .build();
        // {bcrypt}$2a$10$qkB3g30IsUVECKXuIEY7MeLbRr2vhlWgvK.hgUo0zBOhvGUQ.Kp4m
        return new SysUserDetails(user);
    }

}
