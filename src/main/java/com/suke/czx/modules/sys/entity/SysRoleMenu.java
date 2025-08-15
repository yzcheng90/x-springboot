package com.suke.czx.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色与菜单对应关系
 *
 * @author czx
 * @email object_czx@163.com
 */
@Data
@TableName("sys_role_menu")
@Schema(description = "角色与菜单对应关系")
public class SysRoleMenu  implements Serializable {

	@TableId(type = IdType.AUTO)
	public Long id;

	@Schema(description = "角色ID")
	public Long roleId;

	@Schema(description = "菜单ID")
	public Long menuId;

}