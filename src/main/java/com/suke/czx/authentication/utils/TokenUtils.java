package com.suke.czx.authentication.utils;

import cn.hutool.crypto.SecureUtil;
import com.suke.czx.authentication.detail.CustomUserDetailsService;
import com.suke.czx.authentication.detail.CustomUserDetailsUser;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.HttpContextUtils;
import com.suke.czx.common.utils.SpringContextUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author czx
 * @title: TokenUtils
 * @projectName life-helper
 * @description:
 * @date 2023/4/2015:20
 */
@Component
public class TokenUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String generate() {
        String token = SecureUtil.md5(String.valueOf(System.currentTimeMillis()));
        return generate(token);
    }

    public String generate(CustomUserDetailsUser user) {
        String userId = user.getUserId();
        String userName = user.getUsername();
        String auth = userId + "," + userName;
        return generate(auth);
    }

    public String generate(String value) {
        String token = SecureUtil.md5(value + System.currentTimeMillis());
        redisTemplate.opsForValue().set(Constant.AUTHENTICATION_TOKEN + token, value, Constant.TOKEN_EXPIRE, TimeUnit.SECONDS);
        return token;
    }

    public String getUserIdByToken(String token) {
        Object object = redisTemplate.opsForValue().get(Constant.AUTHENTICATION_TOKEN + token);
        if (object instanceof String) {
            String[] split = object.toString().split(",");
            return split[0];
        }
        return null;
    }

    public void deleteToken(String token) {
        redisTemplate.delete(Constant.AUTHENTICATION_TOKEN + token);
    }


    public void generateAuth(String userId) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        generateAuth(request, userId);
    }

    public void generateAuth(HttpServletRequest request, String userId) {
        CustomUserDetailsService customUserDetailsService = SpringContextUtils.getBean(CustomUserDetailsService.class);
        UserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}