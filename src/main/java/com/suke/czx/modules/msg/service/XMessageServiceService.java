package com.suke.czx.modules.msg.service;

import com.suke.czx.modules.msg.entity.XMessageService;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 短信服务
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:51:27
 */
public interface XMessageServiceService extends IService<XMessageService> {

    void saveInfo(XMessageService param);

    void updateInfo(XMessageService param);

    void deleteInfo(XMessageService param);

}
