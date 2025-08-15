package com.suke.czx.interceptor;

import cn.hutool.core.util.StrUtil;
import com.suke.czx.common.exception.CustomAuthenticationException;
import com.suke.czx.common.utils.Constant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author czx
 * @description: 验证码拦截器
 * @date 2019/12/2416:29
 */
@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final RedisTemplate<String,Object> redisTemplate;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public ValidateCodeFilter(RedisTemplate<String,Object> redisTemplate, AuthenticationFailureHandler authenticationFailureHandler){
        this.redisTemplate = redisTemplate;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String url = request.getRequestURI();
        if (pathMatcher.match(Constant.TOKEN_ENTRY_POINT_URL,url)){
            String captcha = request.getParameter("captcha");
            String randomStr = request.getParameter("randomStr");

            if(StrUtil.isBlank(captcha) || StrUtil.isBlank(randomStr)){
                CustomAuthenticationException exception = new CustomAuthenticationException("验证码为空");
                authenticationFailureHandler.onAuthenticationFailure(request,response,exception);
                return;
            }

            String code_key = (String) redisTemplate.opsForValue().get(Constant.NUMBER_CODE_KEY + randomStr);
            if(StrUtil.isEmpty(code_key)){
                CustomAuthenticationException exception = new CustomAuthenticationException("验证码过期");
                authenticationFailureHandler.onAuthenticationFailure(request,response,exception);
                return;
            }

            if(!captcha.equalsIgnoreCase(code_key)){
                CustomAuthenticationException exception = new CustomAuthenticationException("验证码不正确");
                authenticationFailureHandler.onAuthenticationFailure(request,response,exception);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}