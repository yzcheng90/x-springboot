package com.suke.czx.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与权限对应关系
 *
 * @author czx
 * @email object_czx@163.com
 */
@Data
@TableName("sys_role_permission")
@Schema(description = "角色与权限对应关系")
public class SysRolePermission implements Serializable {

	@Schema(description = "角色ID")
	public Long roleId;

	@Schema(description = "权限ID")
	public String permissionId;

}