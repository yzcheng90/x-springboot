package com.suke.czx.modules.tenancy.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.tenancy.entity.TbPlatformTenancy;
import com.suke.czx.modules.tenancy.service.TbPlatformTenancyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 渠道管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2024-11-29 14:48:31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/tenancy/manager")
@Tag(name = "TbPlatformTenancyController", description = "渠道管理")
public class TbPlatformTenancyController extends AbstractController {
    private final TbPlatformTenancyService tbPlatformTenancyService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "渠道管理列表", module = "渠道管理")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<TbPlatformTenancy> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(func -> func.like(TbPlatformTenancy::getTenancyName, keyword).or().like(TbPlatformTenancy::getTenancyRemark, keyword));

        }
        // 如果不是admin 就只能查自己渠道的
        if (!isAdmin()) {
            queryWrapper.lambda().eq(TbPlatformTenancy::getTenancyId, getUserTenancyId());
        }
        queryWrapper.lambda().eq(TbPlatformTenancy::getIsDelete, 0);
        IPage<TbPlatformTenancy> listPage = tbPlatformTenancyService.pageList(mpPageConvert.<TbPlatformTenancy>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }

    /**
     * 获取租户信息
     */
    @GetMapping("/getInfo/{tenancyId}")
    @ResourceAuth(value = "获取租户信息", module = "渠道管理")
    public R getInfo(@PathVariable("tenancyId") Long tenancyId) {
        TbPlatformTenancy tenancy = tbPlatformTenancyService.getById(tenancyId);
        return R.ok().setData(tenancy);
    }

    /**
     * 新增渠道管理
     */
    @SysLog("新增渠道管理数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增渠道管理数据", module = "渠道管理")
    public R save(@RequestBody TbPlatformTenancy param) {
        param.setIsDefault(0);
        param.setCreateTime(new Date());
        param.setUserId(getUserId());
        tbPlatformTenancyService.save(param);
        return R.ok();
    }


    @SysLog("设置默认渠道数据")
    @PostMapping("/setDefault")
    @ResourceAuth(value = "设置默认渠道数据", module = "渠道管理")
    public R setDefault(@RequestBody TbPlatformTenancy param) {
        if (param.getTenancyId() == null) {
            return R.error("参数错误");
        }
        param.setIsDefault(1);
        tbPlatformTenancyService.setDefault(param);
        return R.ok();
    }


    /**
     * 修改
     */
    @SysLog("修改渠道管理数据")
    @PostMapping("/update")
    @ResourceAuth(value = "修改渠道管理数据", module = "渠道管理")
    public R update(@RequestBody TbPlatformTenancy param) {
        param.setIsDefault(0);
        TbPlatformTenancy tenancy = tbPlatformTenancyService.getById(param.getTenancyId());
        if (tenancy == null) {
            return R.error("参数错误");
        }
        if (tenancy.getUserId().equals(getUserId())) {
            tbPlatformTenancyService.updateById(param);
        }
        return R.ok();
    }


    /**
     * 删除
     */
    @SysLog("删除渠道管理数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除渠道管理数据", module = "渠道管理")
    public R delete(@RequestBody TbPlatformTenancy param) {
        TbPlatformTenancy tenancy = tbPlatformTenancyService.getById(param.getTenancyId());
        if (tenancy == null) {
            return R.error("参数错误");
        }
        if (tenancy.getUserId().equals(getUserId())) {
            tbPlatformTenancyService.removeById(param.getTenancyId());
        }
        return R.ok();
    }

    /**
     * 渠道列表
     */
    @GetMapping(value = "/select")
    @ResourceAuth(value = "选择渠道列表", module = "渠道管理")
    public R select() {
        QueryWrapper<TbPlatformTenancy> queryWrapper = new QueryWrapper<>();
        if (!isAdmin()) {
            Integer tenancyId = getUserTenancyId();
            if (tenancyId == null) {
                return R.ok();
            }
            queryWrapper.eq("tenancy_id", tenancyId);
        }
        final List<TbPlatformTenancy> list = tbPlatformTenancyService.list(queryWrapper);
        return R.ok().setData(list);
    }


}
