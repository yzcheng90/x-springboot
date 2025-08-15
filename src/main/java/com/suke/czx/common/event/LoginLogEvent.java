package com.suke.czx.common.event;

import com.suke.czx.modules.sys.entity.SysLoginLog;
import org.springframework.context.ApplicationEvent;

/**
 * @Description 登录日志事件
 * @Date 11:29
 * @Author yzcheng90@qq.com
 **/
public class LoginLogEvent extends ApplicationEvent {

    public LoginLogEvent(SysLoginLog source) {
        super(source);
    }
}