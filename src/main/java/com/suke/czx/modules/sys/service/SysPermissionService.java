package com.suke.czx.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suke.czx.modules.sys.entity.SysPermission;

import java.util.List;

/**
 * 接口权限管理
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2023-05-17 14:48:21
 */
public interface SysPermissionService extends IService<SysPermission> {

    List<SysPermission> getPermissionListByUserId(String userId);

}