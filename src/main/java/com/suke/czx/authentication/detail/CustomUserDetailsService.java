package com.suke.czx.authentication.detail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.suke.czx.modules.sys.entity.SysUser;
import com.suke.czx.modules.sys.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * @Description 自定义用户认证
 * @Date 21:09
 * @Author yzcheng90@qq.com
 **/
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private PermissionsService permissionsService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (ObjectUtil.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getDetail(sysUser);
    }

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getById(userId);
        if (ObjectUtil.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getDetail(sysUser);
    }

    private UserDetails getDetail(SysUser sysUser) {
        Set<String> permissions = permissionsService.getUserPermissions(sysUser.getUserId());
        String[] roles = new String[0];
        if (CollUtil.isNotEmpty(permissions)) {
            roles = permissions.toArray(String[]::new);
        }
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);

        // 是否允许账号登录
        boolean enabled = sysUser.getStatus() == null || sysUser.getStatus() != 0;
        //帐户未过期
        boolean accountNonExpired = true;
        //帐户未锁定
        boolean accountNonLocked = true;
        //证书未过期
        boolean credentialsNonExpired = true;

        return new CustomUserDetailsUser(sysUser.getTenancyId(),
                sysUser.getUserId(),
                sysUser.getUsername(),
                sysUser.getPassword(),
                enabled,
                accountNonExpired,
                accountNonLocked,
                credentialsNonExpired,
                authorities);
    }
}
