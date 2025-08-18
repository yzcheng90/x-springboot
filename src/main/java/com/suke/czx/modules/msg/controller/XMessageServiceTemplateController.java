package com.suke.czx.modules.msg.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.msg.entity.XMessageServiceTemplate;
import com.suke.czx.modules.msg.entity.XMessageServiceTemplateConfig;
import com.suke.czx.modules.msg.service.XMessageServiceTemplateConfigService;
import com.suke.czx.modules.msg.service.XMessageServiceTemplateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 短信模板
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:48:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/template")
@Tag(name = "XMessageServiceTemplateController", description = "短信模板")
public class XMessageServiceTemplateController extends AbstractController {
    private final XMessageServiceTemplateService xMessageServiceTemplateService;
    private final XMessageServiceTemplateConfigService xMessageServiceTemplateConfigService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "短信模板列表", module = "短信模板")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<XMessageServiceTemplate> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(func -> {
                func.like(XMessageServiceTemplate::getTemplateContent, keyword).or().eq(XMessageServiceTemplate::getTenancyName, keyword);
            });
        }

        String tenancyId = MapUtil.getStr(params, "tenancyId");
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(XMessageServiceTemplate::getTenancyId, tenancyId);
        }

        IPage<XMessageServiceTemplate> listPage = xMessageServiceTemplateService.page(mpPageConvert.<XMessageServiceTemplate>pageParamConvert(params), queryWrapper);
        this.fullTenancyInfo(listPage);
        return R.ok().setData(listPage);
    }

    @GetMapping("/select")
    @ResourceAuth(value = "选择短信模板", module = "短信模板")
    public R select(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<XMessageServiceTemplate> queryWrapper = new QueryWrapper<>();

        String tenancyId = MapUtil.getStr(params, "tenancyId");
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(XMessageServiceTemplate::getTenancyId, tenancyId);
        }
        List<XMessageServiceTemplate> list = xMessageServiceTemplateService.list(queryWrapper);
        return R.ok().setData(list);
    }


    /**
     * 新增短信模板
     */
    @SysLog("新增短信模板数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增短信模板数据", module = "短信模板")
    public R save(@RequestBody XMessageServiceTemplate param) {
        xMessageServiceTemplateService.save(param);
        return R.ok();
    }


    /**
     * 修改
     */
    @SysLog("修改短信模板数据")
    @PostMapping("/update")
    @ResourceAuth(value = "修改短信模板数据", module = "短信模板")
    public R update(@RequestBody XMessageServiceTemplate param) {
        xMessageServiceTemplateService.updateById(param);
        return R.ok();
    }


    /**
     * 删除
     */
    @SysLog("删除短信模板数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除短信模板数据", module = "短信模板")
    public R delete(@RequestBody XMessageServiceTemplate param) {
        if (param.getTemplateId() == null) {
            return R.error("模板ID为空");
        }
        long count = xMessageServiceTemplateConfigService.count(Wrappers.<XMessageServiceTemplateConfig>lambdaQuery().eq(XMessageServiceTemplateConfig::getServiceId, param.getTemplateId()));
        if (count > 0) {
            return R.error("当前模板已被使用");
        }
        xMessageServiceTemplateService.removeById(param.getTemplateId());
        return R.ok();
    }

}