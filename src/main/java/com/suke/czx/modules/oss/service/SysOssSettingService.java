package com.suke.czx.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.suke.czx.modules.oss.entity.SysOssSetting;

public interface SysOssSettingService extends IService<SysOssSetting> {

    void setDefault(SysOssSetting param);
}
