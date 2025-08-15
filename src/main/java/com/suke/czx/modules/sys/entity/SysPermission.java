package com.suke.czx.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 接口权限管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-05-17 14:48:21
 */
@Data
@TableName("sys_permission")
public class SysPermission implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "权限ID")
    @JsonProperty(value = "permissionId")
    public String permissionId;

    @Schema(description = "删除状态 0未删除，1已删除")
    @JsonProperty(value = "deleteStatus")
    public Integer deleteStatus;

    @Schema(description = "英文名称")
    @JsonProperty(value = "englishName")
    public String englishName;

    @Schema(description = "模块名称")
    @JsonProperty(value = "moduleName")
    public String moduleName;

    @Schema(description = "权限名称")
    @JsonProperty(value = "name")
    public String name;

    @Schema(description = "URL")
    @JsonProperty(value = "url")
    public String url;

    @Schema(description = "菜单ID")
    @JsonProperty(value = "menuId")
    public String menuId;

    @TableField(exist = false)
    @Schema(description = "菜单ID")
    @JsonProperty(value = "menuName")
    public String menuName;

    @TableField(exist = false)
    public List<SysPermission> children;

}