package com.suke.czx.modules.param.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.suke.czx.modules.param.entity.TbParam;

import java.util.List;

/**
 * 参数管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-04-03 09:19:41
 */
public interface TbParamService extends IService<TbParam> {

    List<TbParam> getParamList(String tenancyId);

    Object getValue(String key);

    Object getValue(String tenancyId, String key);

    Object getObjectValue(String tenancyId, String key, String field);

    Object getValue(Long tenancyId, String key);

    IPage<TbParam> pageInfo(IPage<TbParam> iPage, QueryWrapper<TbParam> queryWrapper);

    void saveInfo(TbParam param);

    void updateInfo(TbParam param);

    void deleteInfo(TbParam param);

}
