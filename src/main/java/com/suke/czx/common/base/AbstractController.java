package com.suke.czx.common.base;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.suke.czx.authentication.detail.CustomUserDetailsUser;
import com.suke.czx.common.utils.HttpContextUtils;
import com.suke.czx.common.utils.MPPageConvert;
import com.suke.czx.common.utils.UserUtil;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import com.suke.czx.modules.tenancy.service.TbPlatformTenancyService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;

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
     *
     * @param params
     * @return
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
     *
     * @return
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
     *
     * @return
     */
    public TbPlatformTenancy getDefaultTenancy() {
        return tbPlatformTenancyService.getDefaultTenancy();
    }
}
