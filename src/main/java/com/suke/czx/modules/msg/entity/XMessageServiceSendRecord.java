package com.suke.czx.modules.msg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.suke.czx.common.annotation.ShardingTable;
import com.suke.czx.modules.tenancy.entity.TenancyBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


/**
 * 短信发送记录（按月分表）
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:40:53
 */
@Data
@ShardingTable
@EqualsAndHashCode(callSuper = true)
@TableName("x_message_service_send_record")
public class XMessageServiceSendRecord extends TenancyBase implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "记录ID")
    @JsonProperty(value = "recordId")
    public Integer recordId;

    @Schema(description = "应用ID")
    @JsonProperty(value = "appId")
    public Integer appId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createTime")
    public Date createTime;

    @Schema(description = "日")
    @JsonProperty(value = "recordDay")
    public Integer recordDay;

    @Schema(description = "月")
    @JsonProperty(value = "recordMonth")
    public Integer recordMonth;

    @Schema(description = "年")
    @JsonProperty(value = "recordYear")
    public Integer recordYear;

    @Schema(description = "内容")
    @JsonProperty(value = "sendContent")
    public String sendContent;

    @Schema(description = "手机号")
    @JsonProperty(value = "sendMobile")
    public String sendMobile;

    @Schema(description = "发送来源")
    @JsonProperty(value = "sendSource")
    public String sendSource;

    @Schema(description = "发送状态")
    @JsonProperty(value = "sendStatus")
    public Integer sendStatus;

    @Schema(description = "发送类型")
    @JsonProperty(value = "sendType")
    public String sendType;

    @Schema(description = "服务ID")
    @JsonProperty(value = "serviceId")
    public Integer serviceId;

    @Schema(description = "模板ID")
    @JsonProperty(value = "templateId")
    public Integer templateId;

}