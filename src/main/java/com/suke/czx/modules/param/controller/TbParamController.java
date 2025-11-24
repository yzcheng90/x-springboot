package com.suke.czx.modules.param.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.param.entity.TbParam;
import com.suke.czx.modules.param.service.TbParamService;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import com.suke.czx.modules.tenancy.service.TbPlatformTenancyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 参数管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-04-03 09:19:41
 */
@RestController
@AllArgsConstructor
@RequestMapping("/param/manager")
@Tag(name = "TbParamController", description = "参数管理")
public class TbParamController extends AbstractController {
    private final TbParamService tbParamService;
    private final TbPlatformTenancyService tbPlatformTenancyService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "参数管理列表", module = "参数管理")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<TbParam> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);

        String paramType = MapUtil.getStr(params, "paramType");

        if (StrUtil.isNotEmpty(paramType)) {
            queryWrapper.lambda().eq(TbParam::getParamType, paramType);
        }

        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(func -> func.like(TbParam::getParamKey, keyword).or().like(TbParam::getParamName, keyword).or().like(TbParam::getParamDescribe, keyword));
        }
        queryWrapper.lambda().orderByDesc(TbParam::getCreateTime);
        IPage<TbParam> listPage = tbParamService.page(mpPageConvert.<TbParam>pageParamConvert(params), queryWrapper);
        if (CollUtil.isNotEmpty(listPage.getRecords())) {
            listPage.getRecords().forEach(item -> {
                TbPlatformTenancy tenancy = tbPlatformTenancyService.getById(item.getTenancyId());
                if (tenancy != null) {
                    item.setTenancyName(tenancy.getTenancyName());
                }
            });
        }
        return R.ok().setData(listPage);
    }


    @GetMapping("/query")
    @ResourceAuth(value = "查询参数信息", module = "参数管理")
    public R query(@RequestParam Map<String, Object> params) {
        QueryWrapper<TbParam> queryWrapper = new QueryWrapper<>();
        String tenancyId = MapUtil.getStr(params, "tenancyId");
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(TbParam::getTenancyId, tenancyId);
        }

        String paramKey = MapUtil.getStr(params, "paramKey");
        if (StrUtil.isNotEmpty(paramKey)) {
            queryWrapper.lambda().eq(TbParam::getParamKey, paramKey);
        }
        queryWrapper.last("limit 1");
        TbParam one = tbParamService.getOne(queryWrapper);
        return R.ok().setData(one);
    }

    /**
     * 新增参数管理
     */
    @SysLog("新增参数管理数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增参数管理数据", module = "参数管理")
    public R save(@RequestBody TbParam tbParam) {
        tbParam.setCreateTime(new Date());
        tbParamService.saveInfo(tbParam);
        return R.ok();
    }


    /**
     * 修改
     */
    @SysLog("修改参数管理数据")
    @PostMapping("/update")
    @ResourceAuth(value = "修改参数管理数据", module = "参数管理")
    public R update(@RequestBody TbParam tbParam) {
        tbParam.setUpdateTime(new Date());
        tbParamService.updateInfo(tbParam);
        return R.ok();
    }


    /**
     * 删除
     */
    @SysLog("删除参数管理数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除参数管理数据", module = "参数管理")
    public R delete(@RequestBody TbParam tbParam) {
        tbParamService.deleteInfo(tbParam);
        return R.ok();
    }

    /**
     * 复制默认渠道参数
     */
    @SysLog("复制默认渠道参数")
    @PostMapping("/copyDefaultParam")
    @ResourceAuth(value = "复制默认渠道参数", module = "参数管理")
    public R copyDefaultParam(@RequestBody TbParam tbParam) {
        Long tenancyId = tbParam.getTenancyId();
        TbPlatformTenancy defaultTenancy = getDefaultTenancy();
        long count = tbParamService.count(Wrappers.<TbParam>query().lambda().eq(TbParam::getTenancyId, tenancyId));
        if (count == 0) {
            List<TbParam> list = tbParamService.list(Wrappers.<TbParam>query().lambda().eq(TbParam::getTenancyId, defaultTenancy.getTenancyId())).stream().map(item -> {
                item.setParamId(null);
                item.setTenancyId(tenancyId);
                return item;
            }).toList();
            tbParamService.saveBatch(list);
        }
        return R.ok();
    }

    /**
     * 复制参数到指定渠道
     */
    @SysLog("复制参数到指定渠道")
    @PostMapping("/copyParam")
    @ResourceAuth(value = "复制参数到指定渠道", module = "参数管理")
    public R copyParam(@RequestBody TbParam tbParam) {
        TbParam param = tbParamService.getById(tbParam.getParamId());
        if (param == null) {
            return R.error("参数不存在");
        }
        Long tenancyId = tbParam.getTenancyId();
        long count = tbParamService.count(Wrappers.<TbParam>query().lambda().eq(TbParam::getTenancyId, tenancyId).eq(TbParam::getParamKey, param.getParamKey()));
        if (count == 0) {
            BeanUtil.copyProperties(param, tbParam);
            tbParam.setCreateTime(new Date());
            tbParam.setParamId(null);
            tbParam.setTenancyId(tenancyId);
            tbParamService.saveInfo(tbParam);
        }
        return R.ok();
    }

    /**
     * 根据租户查询通用任务间隔时间参数
     */
    @GetMapping("/selectParam")
    @ResourceAuth(value = "根据租户查询通用任务间隔时间参数", module = "参数管理")
    public R selectByTenancyId(@RequestParam String tenancyId) {
        TbParam one = tbParamService.getOne(Wrappers.<TbParam>query().lambda().eq(TbParam::getTenancyId, tenancyId).eq(TbParam::getParamKey, "interval_time").last("limit 1"));
        return R.ok().setData(one);
    }
}
