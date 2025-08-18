package com.suke.czx.common.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.authentication.detail.CustomUserDetailsUser;
import com.suke.czx.common.utils.HttpContextUtils;
import com.suke.czx.common.utils.MPPageConvert;
import com.suke.czx.common.utils.UserUtil;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import com.suke.czx.modules.tenancy.entity.TenancyBase;
import com.suke.czx.modules.tenancy.service.TbPlatformTenancyService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

/**
 * Controller公共组件
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2016年11月9日 下午9:42:26
 */

public abstract class AbstractController {

    @Resource
    protected MPPageConvert mpPageConvert;

    @Resource
    public TbPlatformTenancyService tbPlatformTenancyService;

    protected CustomUserDetailsUser getUser() {
        return UserUtil.getUser();
    }

    @SneakyThrows
    protected String getUserId() {
        return UserUtil.getUserId();
    }


    @SneakyThrows
    protected Integer getUserTenancyId() {
        return UserUtil.getUserTenancyId();
    }

    public boolean isAdmin() {
        return UserUtil.isAdmin() || UserUtil.isMiniAdmin();
    }

    public boolean isAdmin(String userId) {
        return UserUtil.isAdmin(userId) || UserUtil.isMiniAdmin(userId);
    }

    /**
     * 优先从参数获取租户
     */
    public String getTenancyId(Map<String, Object> params) {
        String tenancyId = MapUtil.getStr(params, "tenancyId");
        if (StrUtil.isEmpty(tenancyId)) {
            tenancyId = HttpContextUtils.getHttpServletRequest().getHeader("tenancyId");
        }
        if (StrUtil.isEmpty(tenancyId)) {
            TbPlatformTenancy defaultTenancy = getDefaultTenancy();
            if (defaultTenancy != null) {
                tenancyId = defaultTenancy.getTenancyId().toString();
            }
        }
        return tenancyId;
    }

    /**
     * 优先从请求头获取租户
     */
    public String getTenancyId() {
        String tenancyId = HttpContextUtils.getHttpServletRequest().getHeader("tenancyId");
        if (StrUtil.isEmpty(tenancyId)) {
            TbPlatformTenancy defaultTenancy = getDefaultTenancy();
            if (defaultTenancy != null) {
                tenancyId = defaultTenancy.getTenancyId().toString();
            }
        }
        return tenancyId;
    }

    /**
     * 获取默认租户
     */
    public TbPlatformTenancy getDefaultTenancy() {
        return tbPlatformTenancyService.getDefaultTenancy();
    }

    /**
     * 填充租户信息
     */
    public void fullTenancyInfo(List<? extends TenancyBase> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(this::fullTenancyInfo);
        }
    }

    public void fullTenancyInfo(IPage<? extends TenancyBase> listPage) {
        if (CollUtil.isNotEmpty(listPage.getRecords())) {
            this.fullTenancyInfo(listPage.getRecords());
        }
    }

    public void fullTenancyInfo(TenancyBase info) {
        if (info.getTenancyId() != null) {
            TbPlatformTenancy tenancy = tbPlatformTenancyService.getById(info.getTenancyId());
            if (tenancy != null) {
                info.setTenancyName(tenancy.getTenancyName());
            }
        }
    }
}
