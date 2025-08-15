package com.suke.czx.modules.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.suke.czx.authentication.role.PermissionEntity;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.config.PermissionConfig;
import com.suke.czx.modules.sys.entity.SysMenuNew;
import com.suke.czx.modules.sys.entity.SysPermission;
import com.suke.czx.modules.sys.service.SysMenuNewService;
import com.suke.czx.modules.sys.service.SysPermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 接口权限管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-05-17 14:48:21
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sys/permission")
@Tag(name = "SysPermissionController", description = "接口权限管理")
public class SysPermissionController extends AbstractController {
    private final SysPermissionService sysPermissionService;
    private final PermissionConfig permissionConfig;
    private final SysMenuNewService sysMenuNewService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "接口权限管理列表", module = "接口权限管理")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(SysPermission::getName, keyword).or().like(SysPermission::getModuleName, keyword);
        }
        Map<String, List<SysPermission>> list = sysPermissionService.list().stream().collect(Collectors.groupingBy(SysPermission::getModuleName));

        List<SysPermission> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach((key, value) -> {
                SysPermission permission = new SysPermission();
                permission.setModuleName(key);
                permission.setPermissionId(UUID.randomUUID().toString());
                value = value.stream().map(v -> {
                    v.setModuleName(null);
                    return v;
                }).collect(Collectors.toList());
                permission.setChildren(value);
                result.add(permission);
            });
        }
        return R.ok().setData(result);
    }

    /**
     * 列表
     */
    @GetMapping("/menuPermissionTree")
    @ResourceAuth(value = "菜单权限树", module = "接口权限管理")
    public R menuPermissionTree() {
        Map<String, List<SysPermission>> list = sysPermissionService
                .list()
                .stream()
                .map(p -> {
                    // 未配置菜单的权限
                    if (p.getMenuId() == null) {
                        p.setMenuId("null");
                    }
                    return p;
                })
                .collect(Collectors.groupingBy(SysPermission::getMenuId));

        List<HashMap<String, Object>> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            list.forEach((key, value) -> {
                if (!key.equals("null")) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    log.debug("key:{}", key);
                    SysMenuNew menu = sysMenuNewService.getById(key);
                    hashMap.put("label", menu.getTitle());
                    // 这个值不要
                    hashMap.put("permissionId", String.valueOf(System.currentTimeMillis()));
                    hashMap.put("menuId", menu.getMenuId());
                    hashMap.put("children", value.stream().map(per -> {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("label", per.getName());
                        map.put("menuId", per.getMenuId());
                        map.put("permissionId", per.getPermissionId());
                        return map;
                    }).collect(Collectors.toList()));
                    result.add(hashMap);
                }
            });
            // 未配置菜单的权限
            List<SysPermission> permissionList = list.get("null");
            if (CollUtil.isNotEmpty(permissionList)) {
                permissionList.forEach(per -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("label", per.getName());
                    // 这个值不要
                    map.put("menuId", String.valueOf(System.currentTimeMillis()));
                    map.put("permissionId", per.getPermissionId());
                    result.add(map);
                });
            }
        }

        return R.ok().setData(result);
    }

    @PostMapping("/buildMenu")
    @ResourceAuth(value = "权限绑定菜单", module = "接口权限管理")
    public R buildMenu(@RequestBody SysPermission param) {
        if (StrUtil.isEmpty(param.getModuleName())) {
            return R.error("模块名为空");
        }
        if (StrUtil.isEmpty(param.getMenuId())) {
            return R.error("菜单ID为空");
        }

        List<SysPermission> permissions = sysPermissionService.list(Wrappers.<SysPermission>query().lambda().eq(SysPermission::getModuleName, param.getModuleName()));
        if (CollUtil.isNotEmpty(permissions)) {
            permissions.forEach(p -> p.setMenuId(param.getMenuId()));
            sysPermissionService.updateBatchById(permissions);
        }
        return R.ok();
    }

    @PostMapping("/unBuildMenu")
    @ResourceAuth(value = "权限解除绑定菜单", module = "接口权限管理")
    public R unBuildMenu(@RequestBody SysPermission param) {
        if (StrUtil.isEmpty(param.getModuleName())) {
            return R.error("模块名为空");
        }

        List<String> permissions = sysPermissionService
                .list(Wrappers.<SysPermission>query().lambda().eq(SysPermission::getModuleName, param.getModuleName()))
                .stream()
                .map(SysPermission::getPermissionId)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(permissions)) {
            UpdateWrapper<SysPermission> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(SysPermission::getMenuId, null);
            updateWrapper.lambda().in(SysPermission::getPermissionId, permissions);
            sysPermissionService.update(updateWrapper);
        }
        return R.ok();
    }

    /**
     * 权限同步
     */
    @PostMapping("/permissionSync")
    @ResourceAuth(value = "权限同步", module = "接口权限管理")
    public void permissionSync() {
        log.info("开始同步权限");
        List<PermissionEntity> permissionEntities = permissionConfig.getPermissionEntities();
        permissionEntities.forEach(entity -> {
            SysPermission temp = new SysPermission();
            temp.setUrl(entity.getUrl());
            temp.setName(entity.getName());
            temp.setEnglishName(entity.getEnglishName());
            temp.setModuleName(entity.getModuleName());
            temp.setDeleteStatus(0); // 0未删除，1已删除
            SysPermission permission = sysPermissionService.getOne(Wrappers.<SysPermission>query().lambda().eq(SysPermission::getEnglishName, entity.getEnglishName()));
            if (permission != null) {
                temp.setPermissionId(permission.getPermissionId());
                temp.setDeleteStatus(permission.getDeleteStatus());
                sysPermissionService.updateById(temp);
            } else {
                temp.setPermissionId(UUID.randomUUID().toString());
                sysPermissionService.save(temp);
            }
        });
        log.info("结束同步权限");
    }


}