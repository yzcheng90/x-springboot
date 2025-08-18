package com.suke.czx.modules.msg.entity;

import com.suke.czx.modules.tenancy.entity.TenancyBase;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SendMsg extends TenancyBase {

    /**
     * 手机号
     */
    public String mobile;
    /**
     * 验证码
     */
    public String code;

    /**
     * 内容，如果验证码不为空，优先使用验证码进行替换
     */
    public String content;
    /**
     * 服务ID
     */
    public Integer serviceId;

    /**
     * 模板ID
     */
    public Integer templateId;

    /**
     * 模板类型
     */
    public String templateType;

}
