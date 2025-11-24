package com.suke.czx.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.modules.oss.entity.SysOssSetting;
import com.suke.czx.modules.oss.mapper.SysOssSettingMapper;
import com.suke.czx.modules.oss.service.SysOssSettingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysOssSettingServiceImpl extends ServiceImpl<SysOssSettingMapper, SysOssSetting> implements SysOssSettingService {

    @Override
    @Transactional
    public void setDefault(SysOssSetting param) {
        SysOssSetting ossSetting = baseMapper.selectById(param.getId());
        if (ossSetting != null) {
            baseMapper.update(new UpdateWrapper<SysOssSetting>().set("status", 0));
            ossSetting.setStatus(1);
            baseMapper.updateById(ossSetting);
        }
    }
}
