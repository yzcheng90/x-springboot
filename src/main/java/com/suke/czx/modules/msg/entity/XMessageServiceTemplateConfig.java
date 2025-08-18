package com.suke.czx.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * 短信模板配置
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:53:22
 */
@Data
@TableName("x_message_service_template_config")
public class XMessageServiceTemplateConfig implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "服务ID")
    @JsonProperty(value = "serviceId")
    public Integer serviceId;

    @Schema(description = "模板ID")
    @JsonProperty(value = "templateId")
    public Integer templateId;


}