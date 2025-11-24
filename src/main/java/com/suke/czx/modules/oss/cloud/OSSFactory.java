package com.suke.czx.modules.oss.cloud;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.modules.oss.entity.SysOssSetting;
import com.suke.czx.modules.oss.service.SysOssSettingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 文件上传Factory
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2017-03-26 10:18
 */
@Component
@AllArgsConstructor
public class OSSFactory {

    private SysOssSettingService sysOssSettingService;

    public SysOssSetting getConfig() {
        SysOssSetting ossSetting = sysOssSettingService.getOne(Wrappers.<SysOssSetting>query().lambda().eq(SysOssSetting::getStatus, 1));
        if (ossSetting != null) {
            return ossSetting;
        }
        return new SysOssSetting();
    }

    public ICloudStorage build() {
        //获取云存储配置信息
        return cloudStorage(getConfig());
    }

    public ICloudStorage build(String type) {
        SysOssSetting config = sysOssSettingService.getOne(Wrappers.<SysOssSetting>query().lambda().eq(SysOssSetting::getType, type));
        return cloudStorage(config);
    }

    private ICloudStorage cloudStorage(SysOssSetting config) {
        if (config.getType().equals(Constant.CloudService.QINIU.getValue())) {
            return new QiniuCloudStorageService(config);
        } else if (config.getType().equals(Constant.CloudService.MINIO.getValue())) {
            return new MinioCloudStorageService(config);
        }
        return null;
    }

}
