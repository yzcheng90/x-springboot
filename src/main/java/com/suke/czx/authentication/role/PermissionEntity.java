package com.suke.czx.authentication.role;

import lombok.Data;

/**
 * 接口权限类
 */
@Data
public class PermissionEntity {

    /**
     * 接口描述名称
     */
    private String name;
    /**
     * 接口URL名称
     */
    private String englishName;
    /**
     * 完整URL
     */
    private String url;
    /**
     * 模块名
     */
    private String moduleName;

}
