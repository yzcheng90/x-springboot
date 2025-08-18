package com.suke.czx.modules.msg.component;

import com.suke.czx.modules.msg.component.entity.MessageBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SendMessageAbstract implements SendMessage {

    /**
     * 发送短信失败
     * @param messageBody 短信参数
     * @param errorInfo 错误信息
     */
    public void sendMessageFail(MessageBody messageBody, String errorInfo){
        log.error("短信发送失败:{}", errorInfo);
        //FeishuNoticeComponent.sendErrorNotice(entries.getStr("Message"));
    }

}