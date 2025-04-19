package com.suke.czx.modules.tenancy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;

/**
 * 渠道管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-11-29 14:48:31
 */
public interface TbPlatformTenancyService extends IService<TbPlatformTenancy> {

    IPage<TbPlatformTenancy> pageList(IPage<TbPlatformTenancy> tbPlatformTenancyIPage, QueryWrapper<TbPlatformTenancy> queryWrapper);

    void setDefault(TbPlatformTenancy tenancy);

    TbPlatformTenancy getDefaultTenancy();
}
