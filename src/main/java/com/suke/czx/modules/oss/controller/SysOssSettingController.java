package com.suke.czx.modules.oss.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.oss.entity.SysOssSetting;
import com.suke.czx.modules.oss.service.SysOssSettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


/**
 * 文件上传配置
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-12-02 09:34:46
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ossSetting/manager")
@Tag(name = "SysOssSettingController", description = "文件上传配置")
public class SysOssSettingController extends AbstractController {
    private final SysOssSettingService sysOssSettingService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "文件上传配置列表", module = "文件上传配置")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysOssSetting> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().like(SysOssSetting::getType, keyword);
        }
        IPage<SysOssSetting> listPage = sysOssSettingService.page(mpPageConvert.<SysOssSetting>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增文件上传配置
     */
    @SysLog("新增文件上传配置数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增文件上传配置数据", module = "文件上传配置")
    public R save(@RequestBody SysOssSetting param) {
        param.setStatus(0);
        param.setCreateDate(new Date());
        sysOssSettingService.save(param);
        return R.ok();
    }


    /**
     * 修改
     */
    @SysLog("修改文件上传配置数据")
    @PostMapping("/update")
    @ResourceAuth(value = "修改文件上传配置数据", module = "文件上传配置")
    public R update(@RequestBody SysOssSetting param) {
        if (param.getId() == null) {
            return R.error("参数错误");
        }
        param.setStatus(0);
        sysOssSettingService.updateById(param);
        return R.ok();
    }

    @SysLog("设置默认文件上传配置数据")
    @PostMapping("/setDefault")
    @ResourceAuth(value = "设置默认文件上传配置数据", module = "文件上传配置")
    public R setDefault(@RequestBody SysOssSetting param) {
        if (param.getId() == null) {
            return R.error("参数错误");
        }
        param.setStatus(1);
        sysOssSettingService.setDefault(param);
        return R.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除文件上传配置数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除文件上传配置数据", module = "文件上传配置")
    public R delete(@RequestBody SysOssSetting param) {
        sysOssSettingService.removeById(param.getId());
        return R.ok();
    }

}
