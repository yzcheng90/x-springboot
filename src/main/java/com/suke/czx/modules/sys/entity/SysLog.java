package com.suke.czx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * 系统日志
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2017-03-08 10:40:56
 */
@Data
@Schema(description = "系统日志")
public class SysLog {

    @TableId(type = IdType.AUTO)
    public Long id;

    @Schema(description = "用户名")
    public String username;

    @Schema(description = "用户操作")
    public String operation;

    @Schema(description = "请求方法")
    public String method;

    @Schema(description = "请求参数")
    public String params;

    @Schema(description = "执行时长(毫秒)")
    public Long time;

    @Schema(description = "IP地址")
    public String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "创建时间")
    public Date createDate;
}