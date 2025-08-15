package com.suke.czx.authentication.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.suke.czx.authentication.detail.CustomUserDetailsUser;
import com.suke.czx.common.event.LoginLogEvent;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.IPUtils;
import com.suke.czx.common.utils.R;
import com.suke.czx.common.utils.SpringContextUtils;
import com.suke.czx.modules.sys.entity.SysLoginLog;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Date 21:06
 * @Author yzcheng90@qq.com
 **/
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisTemplate<Object,Object> redisTemplate;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token;
        String userId = "";
        String userName = "";
        if (authentication.getPrincipal() instanceof CustomUserDetailsUser) {
            CustomUserDetailsUser userDetailsUser = (CustomUserDetailsUser) authentication.getPrincipal();
            token = SecureUtil.md5(userDetailsUser.getUsername() + System.currentTimeMillis());
            userId = userDetailsUser.getUserId();
            userName = userDetailsUser.getUsername();
        } else {
            token = SecureUtil.md5(String.valueOf(System.currentTimeMillis()));
        }
        // 保存token
        redisTemplate.opsForValue().set(Constant.AUTHENTICATION_TOKEN + token, userId + "," + userName, Constant.TOKEN_EXPIRE, TimeUnit.SECONDS);
        log.info("用户ID:{},用户名:{},登录成功！  token:{}", userId, userName, token);

        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setOptionIp(IPUtils.getIpAddr(request));
        loginLog.setOptionName("用户登录成功");
        loginLog.setOptionTerminal(request.getHeader("User-Agent"));
        loginLog.setUsername(userName);
        loginLog.setOptionTime(new Date());
        SpringContextUtils.publishEvent(new LoginLogEvent(loginLog));

        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(JSONUtil.toJsonStr(R.ok().put(Constant.TOKEN, token)));
    }
}