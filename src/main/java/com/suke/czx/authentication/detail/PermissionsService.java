package com.suke.czx.authentication.detail;

import com.suke.czx.authentication.role.PermissionEntity;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.config.PermissionConfig;
import com.suke.czx.modules.sys.entity.SysPermission;
import com.suke.czx.modules.sys.service.SysPermissionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户角色
 */
@Slf4j
@Service
public class PermissionsService {

    @Resource
    private PermissionConfig permissionConfig;

    @Resource
    private SysPermissionService sysPermissionService;

    public Set<String> getUserPermissions(String userId) {
        Set<String> permissions;
        if (userId.equals(Constant.SUPER_ADMIN)) {
            permissions = permissionConfig.getPermissionEntities().stream().map(PermissionEntity::getEnglishName).collect(Collectors.toSet());
        } else {
            permissions = sysPermissionService.getPermissionListByUserId(userId).stream().map(SysPermission::getEnglishName).collect(Collectors.toSet());
        }
        return permissions;
    }

}