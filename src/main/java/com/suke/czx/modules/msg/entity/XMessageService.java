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
 * 短信服务
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:51:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("x_message_service")
public class XMessageService extends TenancyBase implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "服务ID")
    @JsonProperty(value = "serviceId")
    public Integer serviceId;

    @Schema(description = "AppCode")
    @JsonProperty(value = "appCode")
    public String appCode;

    @Schema(description = "应用ID")
    @JsonProperty(value = "appId")
    public Integer appId;

    @Schema(description = "AppKey")
    @JsonProperty(value = "appKey")
    public String appKey;

    @Schema(description = "AppSecret")
    @JsonProperty(value = "appSecret")
    public String appSecret;

    @Schema(description = "默认模板")
    @JsonProperty(value = "defaultTemplate")
    public String defaultTemplate;

    @Schema(description = "是否使用（1使用，0未使用）")
    @JsonProperty(value = "isEnable")
    public Integer isEnable;

    @Schema(description = "同租户下只能有一个默认服务（1是，0否）")
    @JsonProperty(value = "isDefault")
    public Integer isDefault;

    @Schema(description = "产品ID")
    @JsonProperty(value = "productId")
    public String productId;

    @Schema(description = "服务实现类地址")
    @JsonProperty(value = "serviceClass")
    public String serviceClass;

    @Schema(description = "服务名称")
    @JsonProperty(value = "serviceName")
    public String serviceName;

    @Schema(description = "接口URL")
    @JsonProperty(value = "serviceUrl")
    public String serviceUrl;
    /**
     * 选择的模板
     */
    @TableField(exist = false)
    public Integer[] templateIds;

}