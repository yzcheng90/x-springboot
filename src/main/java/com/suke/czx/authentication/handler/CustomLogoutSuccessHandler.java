package com.suke.czx.authentication.handler;

import cn.hutool.core.util.ObjectUtil;
import com.suke.czx.common.event.LoginLogEvent;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.IPUtils;
import com.suke.czx.common.utils.SpringContextUtils;
import com.suke.czx.modules.sys.entity.SysLoginLog;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author czx
 * @projectName x-springboot
 * @description: 自定义退出成功处理器
 * @date 2019/12/2415:12
 */
@Slf4j
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(Constant.TOKEN);
        Object userInfo = redisTemplate.opsForValue().get(Constant.AUTHENTICATION_TOKEN + token);
        if (ObjectUtil.isNotNull(userInfo)) {
            String[] user = userInfo.toString().split(",");
            if (user.length == 2) {
                SysLoginLog loginLog = new SysLoginLog();
                loginLog.setOptionIp(IPUtils.getIpAddr(request));
                loginLog.setOptionName("用户退出成功");
                loginLog.setOptionTerminal(request.getHeader("User-Agent"));
                loginLog.setUsername(user[1]);
                loginLog.setOptionTime(new Date());
                SpringContextUtils.publishEvent(new LoginLogEvent(loginLog));
            }
        }
        redisTemplate.delete(Constant.AUTHENTICATION_TOKEN + token);
    }
}