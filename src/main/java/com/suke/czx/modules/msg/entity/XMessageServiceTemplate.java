package com.suke.czx.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.suke.czx.modules.tenancy.entity.TenancyBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 短信模板
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:48:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("x_message_service_template")
public class XMessageServiceTemplate extends TenancyBase implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "模板ID")
    @JsonProperty(value = "templateId")
    public Integer templateId;

    @Schema(description = "是否启用，1启动，0禁用")
    @JsonProperty(value = "isEnable")
    public Integer isEnable;

    @Schema(description = "备注")
    @JsonProperty(value = "remark")
    public String remark;

    @Schema(description = "模板内容")
    @JsonProperty(value = "templateContent")
    public String templateContent;

    @Schema(description = "模板类型（预留，比如验证码，营销）")
    @JsonProperty(value = "templateType")
    public String templateType;


}