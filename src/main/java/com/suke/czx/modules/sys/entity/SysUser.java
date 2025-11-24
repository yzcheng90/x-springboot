package com.suke.czx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author czx
 * @email object_czx@163.com
 */
@Data
@TableName("sys_user")
@Schema(description = "系统用户")
public class SysUser implements Serializable {

    public static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    public String userId;

    @Schema(description = "用户名")
    public String username;

    @Schema(description = "姓名")
    public String name;

    @Schema(description = "密码")
    public String password;

    @Schema(description = "邮箱")
    public String email;

    @Schema(description = "手机号")
    public String mobile;

    @Schema(description = "头像")
    public String photo;

    @Schema(description = "状态  0：禁用   1：正常")
    public Integer status;

    @Schema(description = "审核状态  0：拒绝   1：审核中，2：通过")
    public Integer auditStatus;

    @Schema(description = "创建者ID")
    public String createUserId;

    @Schema(description = "租户ID")
    public Integer tenancyId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @TableField(exist = false)
    @Schema(description = "角色ID")
    public List<Long> roleIdList;
}
