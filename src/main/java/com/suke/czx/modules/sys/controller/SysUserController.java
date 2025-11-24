package com.suke.czx.modules.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.HttpContextUtils;
import com.suke.czx.common.utils.IPUtils;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.sys.entity.SysRole;
import com.suke.czx.modules.sys.entity.SysUser;
import com.suke.czx.modules.sys.service.SysMenuNewService;
import com.suke.czx.modules.sys.service.SysRoleService;
import com.suke.czx.modules.sys.service.SysUserService;
import com.suke.czx.modules.sys.vo.RouterInfo;
import com.suke.czx.modules.sys.vo.SysMenuNewVO;
import com.suke.czx.modules.sys.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author czx
 * @email object_czx@163.com
 */
@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
@Tag(name = "SysUserController", description = "系统用户")
public class SysUserController extends AbstractController {

    private final SysUserService sysUserService;
    private final PasswordEncoder passwordEncoder;
    private final SysMenuNewService sysMenuNewService;
    private final SysRoleService sysRoleService;

    /**
     * 所有用户列表
     */
    @GetMapping(value = "/list")
    @ResourceAuth(value = "所有用户列表", module = "系统用户")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();

        //只有超级管理员，才能查看所有管理员列表
        if (!getUserId().equals(Constant.SUPER_ADMIN)) {
            queryWrapper.lambda().eq(SysUser::getCreateUserId, getUserId());
        }

        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper
                    .lambda()
                    .and(func -> func.like(SysUser::getUsername, keyword)
                            .or()
                            .like(SysUser::getMobile, keyword));
        }
        IPage<SysUser> listPage = sysUserService.page(mpPageConvert.<SysUser>pageParamConvert(params), queryWrapper);
        if (CollUtil.isNotEmpty(listPage.getRecords())) {
            listPage.getRecords().forEach(item -> {
                List<Long> roleList = sysRoleService.getRoleListByUserId(item.getUserId()).stream().map(SysRole::getRoleId).toList();
                item.setRoleIdList(roleList);
            });
        }
        return R.ok().setData(listPage);
    }

    /**
     * 获取登录的用户信息和菜单信息
     */
    @GetMapping(value = "/sysInfo")
    @ResourceAuth(value = "获取登录的用户信息和菜单信息", module = "系统用户")
    public R sysInfo() {
        // 用户菜单
        final List<SysMenuNewVO> userMenu = sysMenuNewService.getUserMenu();

        RouterInfo routerInfo = new RouterInfo();
        routerInfo.setMenus(userMenu);

        // 用户信息
        final SysUser sysUser = sysUserService.getById(getUserId());

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(sysUser.getUserId());
        userInfo.setUserName(sysUser.getUsername());
        userInfo.setName(sysUser.getName());
        userInfo.setLoginIp(IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
        final String photo = sysUser.getPhoto();
        userInfo.setPhoto(photo == null ? "https://p9.itc.cn/q_70/images03/20240101/a37b9cd11a4844139183b6ba30758b2f.png" : photo);

        String roles = sysRoleService.getRoleListByUserId(sysUser.getUserId()).stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
        userInfo.setRoleNames(StrUtil.isEmpty(roles) ? "管理员" : roles);
        userInfo.setRoles(new String[]{sysUser.getUserId().equals(Constant.SUPER_ADMIN) ? "admin" : "common"});
        userInfo.setTime(DateUtil.now());
        userInfo.setAuthBtnList(new String[]{"btn.add", "btn.del", "btn.edit", "btn.link"});
        routerInfo.setUserInfo(userInfo);
        return R.ok().setData(routerInfo);
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResourceAuth(value = "修改密码", module = "系统用户")
    public R password(@RequestBody Map<String, Object> param) {
        String password = MapUtil.getStr(param, "password");
        String newPassword = MapUtil.getStr(param, "newPassword");
        if (StrUtil.isEmpty(newPassword)) {
            return R.error("新密码不为能空");
        }
        newPassword = passwordEncoder.encode(newPassword);

        SysUser user = sysUserService.getById(getUserId());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return R.error("原密码不正确");
        }
        //更新密码
        sysUserService.updatePassword(getUserId(), password, newPassword);
        return R.ok();
    }

    @SysLog("审核用户")
    @PostMapping(value = "/audit")
    @ResourceAuth(value = "审核用户", module = "系统用户")
    public R audit(@RequestBody SysUser user) {
        String userId = user.getUserId();
        if (StrUtil.isEmpty(userId)) {
            return R.error("用户ID为空");
        }
        Integer auditStatus = user.getAuditStatus();
        if (auditStatus == null) {
            return R.error("审核状态为空");
        }

        SysUser sysUser = sysUserService.getById(userId);
        if (auditStatus == 2) {
            sysUser.setStatus(1);
        }
        sysUser.setAuditStatus(auditStatus);
        sysUserService.updateById(sysUser);
        return R.ok();
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping(value = "/save")
    @ResourceAuth(value = "保存用户", module = "系统用户")
    public R save(@RequestBody @Validated SysUser user) {
        long usernameCount = sysUserService.count(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, user.getUsername()));
        if (usernameCount > 0) {
            return R.error("用户名已存在");
        }
        long mobileCount = sysUserService.count(Wrappers.<SysUser>query().lambda().eq(SysUser::getMobile, user.getMobile()));
        if (mobileCount > 0) {
            return R.error("手机号已注册");
        }
        long emailCount = sysUserService.count(Wrappers.<SysUser>query().lambda().eq(SysUser::getEmail, user.getEmail()));
        if (emailCount > 0) {
            return R.error("邮箱已注册");
        }
        user.setCreateUserId(getUserId());
        user.setAuditStatus(2);//通过
        sysUserService.saveUserRole(user);
        return R.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PostMapping(value = "/update")
    @ResourceAuth(value = "修改用户", module = "系统用户")
    public R update(@RequestBody @Validated SysUser user) {
        sysUserService.updateUserRole(user);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @PostMapping(value = "/delete")
    @ResourceAuth(value = "删除用户", module = "系统用户")
    public R delete(@RequestBody SysUser user) {
        if (user == null || user.getUserId() == null) {
            return R.error("参数错误");
        }

        if (user.getUserId().equals(Constant.SUPER_ADMIN)) {
            return R.error("系统管理员不能删除");
        }

        if (user.getUserId().equals(getUserId())) {
            return R.error("当前用户不能删除");
        }
        sysUserService.removeById(user.getUserId());
        return R.ok();
    }
}