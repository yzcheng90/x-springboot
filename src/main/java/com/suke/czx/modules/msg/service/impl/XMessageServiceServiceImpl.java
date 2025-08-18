package com.suke.czx.modules.msg.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.modules.msg.entity.XMessageService;
import com.suke.czx.modules.msg.entity.XMessageServiceTemplateConfig;
import com.suke.czx.modules.msg.mapper.XMessageServiceMapper;
import com.suke.czx.modules.msg.mapper.XMessageServiceTemplateConfigMapper;
import com.suke.czx.modules.msg.service.XMessageServiceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


/**
 * 短信服务
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:51:27
 */
@Service
@AllArgsConstructor
public class XMessageServiceServiceImpl extends ServiceImpl<XMessageServiceMapper, XMessageService> implements XMessageServiceService {

    private final XMessageServiceTemplateConfigMapper xMessageServiceTemplateConfigMapper;

    @Override
    @Transactional
    public void saveInfo(XMessageService param) {
        baseMapper.insert(param);
        this.saveConfig(param);
    }

    @Override
    @Transactional
    public void updateInfo(XMessageService param) {
        baseMapper.updateById(param);
        if (param.getTemplateIds() != null) {
            xMessageServiceTemplateConfigMapper.delete(Wrappers.<XMessageServiceTemplateConfig>lambdaQuery().eq(XMessageServiceTemplateConfig::getServiceId, param.getServiceId()));
            this.saveConfig(param);
        }
    }

    @Override
    @Transactional
    public void deleteInfo(XMessageService param) {
        xMessageServiceTemplateConfigMapper.delete(Wrappers.<XMessageServiceTemplateConfig>lambdaQuery().eq(XMessageServiceTemplateConfig::getServiceId, param.getServiceId()));
        baseMapper.deleteById(param.getServiceId());
    }

    @Transactional
    public void saveConfig(XMessageService param) {
        if (param.getTemplateIds() != null) {
            List<XMessageServiceTemplateConfig> configs = Arrays.stream(param.getTemplateIds()).map(item -> {
                XMessageServiceTemplateConfig config = new XMessageServiceTemplateConfig();
                config.setServiceId(param.getServiceId());
                config.setTemplateId(item);
                return config;
            }).toList();
            if (CollUtil.isNotEmpty(configs)) {
                xMessageServiceTemplateConfigMapper.insert(configs);
            }
        }
    }
}
