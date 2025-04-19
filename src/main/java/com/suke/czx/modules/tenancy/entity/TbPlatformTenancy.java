package com.suke.czx.modules.tenancy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 渠道管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-11-29 14:48:31
 */
@Data
@TableName("tb_platform_tenancy")
public class TbPlatformTenancy implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "渠道ID")
    @JsonProperty(value = "tenancyId")
    public Integer tenancyId;

    @Schema(description = "渠道名称")
    @JsonProperty(value = "tenancyName")
    public String tenancyName;

    @Schema(description = "渠道备注")
    @JsonProperty(value = "tenancyRemark")
    public String tenancyRemark;

    @Schema(description = "折扣")
    @JsonProperty(value = "discount")
    public Double discount;

    @Schema(description = "是否默认租户")
    @JsonProperty(value = "isDefault")
    public Integer isDefault;

    @Schema(description = "广告位配置")
    @JsonProperty(value = "ex")
    public String ex;

    @TableLogic
    @Schema(description = "是否删除")
    @JsonProperty(value = "isDelete")
    public Integer isDelete;

    @Schema(description = "是否更新数据（1：是，0：否）")
    @JsonProperty(value = "updateData")
    public Integer updateData;

    @Schema(description = "创建人ID")
    @JsonProperty(value = "userId")
    public String userId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createTime")
    public Date createTime;

    @Schema(description = "父级租户ID")
    @JsonProperty(value = "tenancyPid")
    public Integer tenancyPid;

    @TableField(exist = false)
    @Schema(description = "父级租户名称")
    @JsonProperty(value = "tenancyParentName")
    public String tenancyParentName;


}
