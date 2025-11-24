package com.suke.czx.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.modules.sys.entity.SysRole;
import com.suke.czx.modules.sys.entity.SysRoleMenu;
import com.suke.czx.modules.sys.entity.SysRolePermission;
import com.suke.czx.modules.sys.mapper.SysRoleMapper;
import com.suke.czx.modules.sys.mapper.SysRoleMenuMapper;
import com.suke.czx.modules.sys.mapper.SysRolePermissionMapper;
import com.suke.czx.modules.sys.service.SysRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2019年4月17日
 */
@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    @Transactional
    public void saveRoleMenu(SysRole role) {
        role.setCreateTime(new Date());
        sysRoleMapper.insert(role);
        if (CollUtil.isNotEmpty(role.getMenuIdList())) {
            List<SysRoleMenu> sysRoleMenus = role.getMenuIdList().stream().distinct().map(id -> {
                SysRoleMenu menu = new SysRoleMenu();
                menu.setMenuId(id);
                menu.setRoleId(role.getRoleId());
                return menu;
            }).collect(Collectors.toList());
            sysRoleMenuMapper.insert(sysRoleMenus);
        }
        if (CollUtil.isNotEmpty(role.getPermissionList())) {
            this.saveRolePermission(role);
        }
    }

    @Override
    @Transactional
    public void updateRoleMenu(SysRole role) {
        sysRoleMapper.updateById(role);
        if (CollUtil.isNotEmpty(role.getMenuIdList())) {
            sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, role.getRoleId()));
            List<SysRoleMenu> sysRoleMenus = role.getMenuIdList().stream().distinct().map(id -> {
                SysRoleMenu menu = new SysRoleMenu();
                menu.setMenuId(id);
                menu.setRoleId(role.getRoleId());
                return menu;
            }).collect(Collectors.toList());
            sysRoleMenuMapper.insert(sysRoleMenus);
        }

        if (CollUtil.isNotEmpty(role.getPermissionList())) {
            sysRolePermissionMapper.delete(Wrappers.<SysRolePermission>query().lambda().eq(SysRolePermission::getRoleId, role.getRoleId()));
            this.saveRolePermission(role);
        }

    }

    public void saveRolePermission(SysRole role) {
        List<SysRolePermission> sysPermissions = role.getPermissionList().stream().distinct().map(id -> {
            SysRolePermission menu = new SysRolePermission();
            menu.setPermissionId(id);
            menu.setRoleId(role.getRoleId());
            return menu;
        }).collect(Collectors.toList());
        sysRolePermissionMapper.insert(sysPermissions);
    }

    @Override
    public List<SysRole> getRoleListByUserId(String userId) {
        return baseMapper.getRoleListByUserId(userId);
    }

    @Override
    public List<Long> queryRoleIdList(String createUserId) {
        return sysRoleMapper.queryRoleIdList(createUserId);
    }

    @Override
    public List<Long> queryUserRoleIdList(String userId) {
        return baseMapper.queryUserRoleIdList(userId);
    }


    @Override
    public void deleteBath(Long id) {
        baseMapper.deleteById(id);
        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, id));

    }

}