package com.suke.czx.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.suke.czx.authentication.detail.CustomUserDetailsService;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.R;
import com.suke.czx.common.utils.SpringContextUtils;
import com.suke.czx.config.AuthIgnoreConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @Description
 * @Date 22:23
 * @Author yzcheng90@qq.com
 **/
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisTemplate<String,Object> redisTemplate;
    private final AuthIgnoreConfig authIgnoreConfig;

    public AuthenticationTokenFilter(AuthIgnoreConfig authIgnoreConfig,RedisTemplate<String,Object> template) {
        this.redisTemplate = template;
        this.authIgnoreConfig = authIgnoreConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(Constant.TOKEN);
        if (StrUtil.isBlank(token) || StrUtil.equals(token, "null")) {
            token = request.getParameter(Constant.TOKEN);
        }

        if (StrUtil.isNotBlank(token) && !StrUtil.equals(token, "null")) {
            final String requestURI = request.getRequestURI();
            if(!authIgnoreConfig.isContains(requestURI)){
                Object userInfo = redisTemplate.opsForValue().get(Constant.AUTHENTICATION_TOKEN + token);
                if (ObjectUtil.isNull(userInfo)) {
                    writer(response, "无效token");
                    return;
                }
                String[] user = userInfo.toString().split(",");
                if (user.length != 2) {
                    writer(response, "无效token");
                    return;
                }

                String userId = user[0];
                CustomUserDetailsService customUserDetailsService = SpringContextUtils.getBean(CustomUserDetailsService.class);
                UserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }


    @SneakyThrows
    public void writer(HttpServletResponse response, String msg) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JSONUtil.toJsonStr(R.error(HttpServletResponse.SC_UNAUTHORIZED, msg)));
    }
}