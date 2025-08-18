package com.suke.czx.modules.msg.service;

import com.suke.czx.modules.msg.entity.SendMsg;
import com.suke.czx.modules.msg.entity.XMessageService;
import com.suke.czx.modules.msg.entity.XMessageServiceSendRecord;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 短信发送记录（按月分表）
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:40:53
 */
public interface XMessageServiceSendRecordService extends IService<XMessageServiceSendRecord> {

    boolean autoServiceSendMessage(SendMsg msg);

    boolean sendMessage(XMessageService messageService, SendMsg msg);

}
