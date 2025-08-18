package com.suke.czx.modules.msg.component.entity;

import com.suke.czx.modules.msg.entity.XMessageService;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class MessageBody extends XMessageService {

    public String mobile;
    public String code;
    public String template;

}
