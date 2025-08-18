package com.suke.czx.modules.application.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.application.entity.XApplication;
import com.suke.czx.modules.application.service.XApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 应用服务
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 16:34:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/application/manager")
@Tag(name = "XApplicationController", description = "应用服务")
public class XApplicationController extends AbstractController {
    private final XApplicationService xApplicationService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "应用服务列表", module = "应用服务")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<XApplication> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {

        }
        IPage<XApplication> listPage = xApplicationService.page(mpPageConvert.<XApplication>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }

    @AuthIgnore
    @GetMapping("/generateKey")
    public R generateKey() {
        String string = SecureUtil.md5(String.valueOf(System.currentTimeMillis()));
        return R.ok().setData(string);
    }

    @AuthIgnore
    @GetMapping("/select")
    public R select() {
        List<XApplication> list = xApplicationService.list();
        return R.ok().setData(list);
    }

    /**
     * 新增应用服务
     */
    @SysLog("新增应用服务数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增应用服务数据", module = "应用服务")
    public R save(@RequestBody XApplication param) {
        xApplicationService.save(param);
        return R.ok();
    }


    /**
     * 修改
     */
    @SysLog("修改应用服务数据")
    @PostMapping("/update")
    @ResourceAuth(value = "修改应用服务数据", module = "应用服务")
    public R update(@RequestBody XApplication param) {
        xApplicationService.updateById(param);
        return R.ok();
    }


    /**
     * 删除
     */
    @SysLog("删除应用服务数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除应用服务数据", module = "应用服务")
    public R delete(@RequestBody XApplication param) {
        xApplicationService.removeById(param.getAppId());
        return R.ok();
    }

}