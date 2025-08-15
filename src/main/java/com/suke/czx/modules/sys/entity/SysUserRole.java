package com.suke.czx.modules.sys.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户与角色对应关系
 *
 * @author czx
 * @email object_czx@163.com
 */
@Data
@Schema(description = "用户与角色对应关系")
public class SysUserRole implements Serializable {

    @TableId
    public Long id;

    @Schema(description = "用户ID")
    public String userId;

    @Schema(description = "角色ID")
    public Long roleId;

}