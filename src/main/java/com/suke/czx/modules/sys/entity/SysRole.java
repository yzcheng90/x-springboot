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
 * 角色
 *
 * @author czx
 * @email object_czx@163.com
 */
@Data
@Schema(description = "角色")
@TableName("sys_role")
public class SysRole implements Serializable {

    public static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    public Long roleId;

    @Schema(description = "角色名称")
    public String roleName;

    @Schema(description = "备注")
    public String remark;

    @Schema(description = "创建者ID")
    public String createUserId;

    @TableField(exist = false)
    @Schema(description = "创建者名称")
    public String createUserName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date createTime;

    @Schema(description = "菜单ID")
    @TableField(exist = false)
    public List<Long> menuIdList;

    @Schema(description = "权限ID")
    @TableField(exist = false)
    public List<String> permissionList;
}