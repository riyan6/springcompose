package org.spring.compose.common.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {

    private Long id;
    // private Long tenantId;
    private String gender;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String mobile;
    private String email;
    private String name;
    private Integer dataScope;
    private Set<String> roles;
    /**
     * 用户权限标识集合
     */
    private Set<String> perms;
    // private String status;
    // private Boolean isSuperAdmin;

}
