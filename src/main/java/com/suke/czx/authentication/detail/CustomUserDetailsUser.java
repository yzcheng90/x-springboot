package com.suke.czx.authentication.detail;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Description : 自定义UserDetails
 * @Date 21:13
 * @Author yzcheng90@qq.com
 **/
@Getter
public class CustomUserDetailsUser extends User implements Serializable {

    private final String userId;
    private final Integer tenancyId;

    public CustomUserDetailsUser(Integer tenancyId, String userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.tenancyId = tenancyId;
    }

    public CustomUserDetailsUser(Integer tenancyId, String userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.tenancyId = tenancyId;
    }

}
