package com.suke.czx.modules.param.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 参数管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-04-03 09:19:41
 */
@Data
@TableName("sys_param")
public class TbParam implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    @Schema(description = "参数ID")
    @JsonProperty(value = "paramId")
    @JsonSerialize(using = ToStringSerializer.class)
    public String paramId;

    @Schema(description = "参数名称")
    @JsonProperty(value = "paramName")
    public String paramName;

    @Schema(description = "参数类型")
    @JsonProperty(value = "paramType")
    public String paramType;

    @Schema(description = "租户ID")
    @JsonProperty(value = "tenancyId")
    @JsonSerialize(using = ToStringSerializer.class)
    public Long tenancyId;

    @TableField(exist = false)
    @Schema(description = "租户名称")
    @JsonProperty(value = "tenancyName")
    public String tenancyName;

    @Schema(description = "参数描述")
    @JsonProperty(value = "paramDescribe")
    public String paramDescribe;

    @Schema(description = "参数KEY")
    @JsonProperty(value = "paramKey")
    public String paramKey;

    @Schema(description = "参数VALUE")
    @JsonProperty(value = "paramValue")
    public String paramValue;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createTime")
    public Date createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "updateTime")
    public Date updateTime;


}
