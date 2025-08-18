package com.suke.czx.modules.msg.component;


import com.suke.czx.modules.msg.component.entity.MessageBody;

public interface SendMessage {

    boolean sendMessage(MessageBody messageBody);
}