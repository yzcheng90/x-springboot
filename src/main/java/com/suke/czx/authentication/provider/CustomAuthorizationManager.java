package com.suke.czx.authentication.provider;

import cn.hutool.core.collection.CollUtil;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.URLConvertUtil;
import com.suke.czx.common.utils.UserUtil;
import com.suke.czx.config.AuthIgnoreConfig;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;


/**
 * 自定义授权管理器
 */
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Resource
    private AuthIgnoreConfig authIgnoreConfig;


    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        String requestUrl = context.getRequest().getServletPath();
        Collection<? extends GrantedAuthority> authorities = authentication.get().getAuthorities();

        // 如果URL中包含参数，则把参数去掉
        if (requestUrl.contains("?")) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
        }

        // 如果URL是不需要认证的 直接返回通过
        List<String> ignoreUrls = authIgnoreConfig.getIgnoreUrls();
        if (CollUtil.isNotEmpty(ignoreUrls) && authIgnoreConfig.match(requestUrl)) {
            // 如果是忽略认证的设置为authIgnore
            return new AuthorizationDecision(true);
        }

        // 如果是匿名用户则直接返回不通过
        if (authentication.get() instanceof AnonymousAuthenticationToken) {
            return new AuthorizationDecision(false);
        }

        String userId = UserUtil.getUserId();
        // 如果是超级管理员则直接返回通过
        if (userId.equals(Constant.SUPER_ADMIN)) {
            return new AuthorizationDecision(true);
        } else {
            String role = URLConvertUtil.convert(requestUrl);
            for (GrantedAuthority authority : authorities) {
                //判断当前用户是否有对应权限，有则放行
                if (role.equals(authority.getAuthority())) {
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        }
    }
}