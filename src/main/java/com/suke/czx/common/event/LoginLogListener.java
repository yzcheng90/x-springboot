package com.suke.czx.common.event;

import com.suke.czx.modules.sys.entity.SysLoginLog;
import com.suke.czx.modules.sys.service.SysLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @Description 登录日志监听器
 * @Date 11:28
 * @Author yzcheng90@qq.com
 **/
@Configuration
@RequiredArgsConstructor
public class LoginLogListener {

    private final SysLoginLogService sysLoginLogService;

    @Async
    @EventListener(LoginLogEvent.class)
    public void saveSysLog(LoginLogEvent event) {
        SysLoginLog sysLoginLog = (SysLoginLog) event.getSource();
        sysLoginLogService.save(sysLoginLog);
    }

}