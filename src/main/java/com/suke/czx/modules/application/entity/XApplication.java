package com.suke.czx.modules.application.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * 应用服务
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 16:34:08
 */
@Data
@TableName("x_application")
public class XApplication implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "应用ID")
    @JsonProperty(value = "appId")
    public Integer appId;

    @Schema(description = "AppKey")
    @JsonProperty(value = "appKey")
    public String appKey;

    @Schema(description = "应用名称")
    @JsonProperty(value = "appName")
    public String appName;

    @Schema(description = "AppSecret")
    @JsonProperty(value = "appSecret")
    public String appSecret;

    @Schema(description = "是否可用（1可用，0不可用）")
    @JsonProperty(value = "isEnable")
    public Integer isEnable;

    @Schema(description = "租户ID")
    @JsonProperty(value = "tenancyId")
    public String tenancyId;


}