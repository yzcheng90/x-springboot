package com.suke.czx.authentication.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.json.JSONUtil;
import com.suke.czx.common.event.LoginLogEvent;
import com.suke.czx.common.utils.IPUtils;
import com.suke.czx.common.utils.R;
import com.suke.czx.common.utils.SpringContextUtils;
import com.suke.czx.modules.sys.entity.SysLoginLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

/**
 * @Description
 * @Date 21:05
 * @Author yzcheng90@qq.com
 **/
@Slf4j
@Component
public class CustomAuthenticationFailHandler implements AuthenticationFailureHandler {

    @SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception){

        final String username = request.getParameter("username");
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setOptionIp(IPUtils.getIpAddr(request));
        loginLog.setOptionName("用户登录失败");
        loginLog.setOptionTerminal(request.getHeader("User-Agent"));
        loginLog.setUsername(username);
        SpringContextUtils.publishEvent(new LoginLogEvent(loginLog));

        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter printWriter = response.getWriter();

        if(exception instanceof DisabledException){
            printWriter.append(JSONUtil.toJsonStr(R.error("账户被禁用，请联系管理员")));
        }else {
            printWriter.append(JSONUtil.toJsonStr(R.error(exception.getMessage())));
        }
    }
}