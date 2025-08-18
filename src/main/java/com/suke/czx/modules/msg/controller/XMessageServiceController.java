package com.suke.czx.modules.msg.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.msg.entity.XMessageService;
import com.suke.czx.modules.msg.service.XMessageServiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 短信服务
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:51:27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/service")
@Tag(name = "XMessageServiceController", description = "短信服务")
public class XMessageServiceController extends AbstractController {
    private final XMessageServiceService xMessageServiceService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "短信服务列表", module = "短信服务")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<XMessageService> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(func -> func.like(XMessageService::getServiceName, keyword).or().eq(XMessageService::getDefaultTemplate, keyword));
        }

        String tenancyId = MapUtil.getStr(params, "tenancyId");
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(XMessageService::getTenancyId, tenancyId);
        }

        IPage<XMessageService> listPage = xMessageServiceService.page(mpPageConvert.<XMessageService>pageParamConvert(params), queryWrapper);
        this.fullTenancyInfo(listPage);
        return R.ok().setData(listPage);
    }


    /**
     * 新增短信服务
     */
    @SysLog("新增短信服务数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增短信服务数据", module = "短信服务")
    public R save(@RequestBody XMessageService param) {
        xMessageServiceService.saveInfo(param);
        return R.ok();
    }


    /**
     * 修改
     */
    @SysLog("修改短信服务数据")
    @PostMapping("/update")
    @ResourceAuth(value = "修改短信服务数据", module = "短信服务")
    public R update(@RequestBody XMessageService param) {
        if (param.getServiceId() == null) {
            return R.error("服务ID为空");
        }
        xMessageServiceService.updateInfo(param);
        return R.ok();
    }


    /**
     * 删除
     */
    @SysLog("删除短信服务数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除短信服务数据", module = "短信服务")
    public R delete(@RequestBody XMessageService param) {
        if (param.getServiceId() == null) {
            return R.error("服务ID为空");
        }
        xMessageServiceService.deleteInfo(param);
        return R.ok();
    }

}