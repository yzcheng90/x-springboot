package com.suke.czx.modules.tenancy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import com.suke.czx.modules.tenancy.mapper.TbPlatformTenancyMapper;
import com.suke.czx.modules.tenancy.service.TbPlatformTenancyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 渠道管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-11-29 14:48:31
 */
@Service
public class TbPlatformTenancyServiceImpl extends ServiceImpl<TbPlatformTenancyMapper, TbPlatformTenancy> implements TbPlatformTenancyService {

    @Override
    public IPage<TbPlatformTenancy> pageList(IPage<TbPlatformTenancy> tbPlatformTenancyIPage, QueryWrapper<TbPlatformTenancy> queryWrapper) {
        return baseMapper.pageList(tbPlatformTenancyIPage, queryWrapper);
    }

    @Override
    @Transactional
    public void setDefault(TbPlatformTenancy tenancy) {
        TbPlatformTenancy tbPlatformTenancy = baseMapper.selectById(tenancy.getTenancyId());
        if (tbPlatformTenancy != null) {
            baseMapper.update(new UpdateWrapper<TbPlatformTenancy>().set("is_default", 0));
            tenancy.setIsDefault(1);
            baseMapper.updateById(tenancy);
        }
    }

    @Override
    public TbPlatformTenancy getDefaultTenancy() {
        return baseMapper.getDefaultTenancy();
    }
}
