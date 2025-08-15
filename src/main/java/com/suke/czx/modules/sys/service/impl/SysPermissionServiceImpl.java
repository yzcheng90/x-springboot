package com.suke.czx.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.modules.sys.entity.SysPermission;
import com.suke.czx.modules.sys.mapper.SysPermissionMapper;
import com.suke.czx.modules.sys.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 接口权限管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-05-17 14:48:21
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermission> getPermissionListByUserId(String userId) {
        return baseMapper.getPermissionListByUserId(userId);
    }
}