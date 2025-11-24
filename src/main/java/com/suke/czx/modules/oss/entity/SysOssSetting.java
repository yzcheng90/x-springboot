package com.suke.czx.modules.oss.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传配置
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-12-02 09:34:46
 */
@Data
@TableName("sys_oss_setting")
public class SysOssSetting implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    @JsonProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long id;

    @Schema(description = "URL地址")
    @JsonProperty(value = "url")
    public String url;

    @Schema(description = "类型")
    @JsonProperty(value = "type")
    public String type;

    @Schema(description = "访问密钥")
    @JsonProperty(value = "accessKey")
    public String accessKey;

    @Schema(description = "密钥")
    @JsonProperty(value = "secretKey")
    public String secretKey;

    @Schema(description = "桶名称")
    @JsonProperty(value = "bucketName")
    public String bucketName;

    @Schema(description = "view")
    @JsonProperty(value = "view")
    public String view;

    @Schema(description = "前缀")
    @JsonProperty(value = "prefix")
    public String prefix;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createDate")
    public Date createDate;

    @Schema(description = "1:默认 0可选")
    @JsonProperty(value = "status")
    public Integer status;


}
