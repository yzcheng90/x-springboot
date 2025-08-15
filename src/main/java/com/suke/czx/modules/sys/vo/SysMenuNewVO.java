package com.suke.czx.modules.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : 菜单管理
 * @Date 12:26
 * @Author yzcheng90@qq.com
 **/
@Data
@Schema(description = "菜单管理")
public class SysMenuNewVO implements Serializable {

    public Long menuId;
    public Long parentId;
    public String path;
    public String name;
    public String menuType;
    public String component;
    public String redirect;
    public String title;
    public int orderSort;
    public boolean isLink;

    public RouterMetaVO meta;

    public List<SysMenuNewVO> children;

}