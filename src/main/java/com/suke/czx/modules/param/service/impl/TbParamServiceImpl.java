package com.suke.czx.modules.param.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.suke.czx.common.utils.HttpContextUtils;
import com.suke.czx.modules.param.entity.TbParam;
import com.suke.czx.modules.param.mapper.TbParamMapper;
import com.suke.czx.modules.param.service.TbParamService;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import com.suke.czx.modules.tenancy.mapper.TbPlatformTenancyMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 参数管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-04-03 09:19:41
 */
@Service
@AllArgsConstructor
public class TbParamServiceImpl extends ServiceImpl<TbParamMapper, TbParam> implements TbParamService {

    private final TbPlatformTenancyMapper tbPlatformTenancyMapper;


    @Override
    public List<TbParam> getParamList(String tenancyId) {
        QueryWrapper<TbParam> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(TbParam::getTenancyId, tenancyId);
        }
        queryWrapper.lambda().orderByDesc(TbParam::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Object getValue(String key) {
        // 获取租户PID
        String tenancyId = HttpContextUtils.getHttpServletRequest().getHeader("tenancyId");
        if (StrUtil.isEmpty(tenancyId)) {
            tenancyId = "1";
        }
        return this.getValue(tenancyId, key);
    }

    @Override
    public Object getValue(Long tenancyId, String key) {
        if (tenancyId == null) {
            return null;
        }
        return this.getValue(String.valueOf(tenancyId), key);
    }

    public Object getValue(String tenancyId, String key) {
        QueryWrapper<TbParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TbParam::getParamKey, key).last("limit 1");
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(TbParam::getTenancyId, tenancyId);
        } else {
            // 如果当前请求没有租户，就获取默认租户的
            TbPlatformTenancy defaultTenancy = tbPlatformTenancyMapper.getDefaultTenancy();
            if (defaultTenancy != null) {
                queryWrapper.lambda().eq(TbParam::getTenancyId, defaultTenancy.getTenancyId());
            }
        }
        TbParam tbParam = baseMapper.selectOne(queryWrapper);
        if (tbParam != null) {
            return tbParam.getParamValue();
        }
        return null;
    }

    @Override
    public Object getObjectValue(String tenancyId, String key, String field) {
        Object productOrderTimeConfig = this.getValue(tenancyId, key);
        if (productOrderTimeConfig != null) {
            JSONObject entries = JSONUtil.parseObj(productOrderTimeConfig);
            if (entries != null) {
                return entries.get(field);
            }
        }
        return null;
    }

    @Override
    public IPage<TbParam> pageInfo(IPage<TbParam> iPage, QueryWrapper<TbParam> queryWrapper) {
        return baseMapper.selectPage(iPage, queryWrapper);
    }

    @Override
    public void saveInfo(TbParam param) {
        baseMapper.insert(param);
    }

    @Override
    public void updateInfo(TbParam param) {
        baseMapper.updateById(param);
    }

    @Override
    public void deleteInfo(TbParam param) {
        baseMapper.deleteById(param.getParamId());
    }
}
