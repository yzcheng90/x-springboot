package com.suke.czx.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.modules.sys.entity.SysLog;
import com.suke.czx.modules.sys.entity.SysLoginLog;
import com.suke.czx.modules.sys.service.SysLogService;
import com.suke.czx.modules.sys.service.SysLoginLogService;
import com.suke.czx.common.utils.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 系统日志
 *
 * @author czx
 * @email object_czx@163.com
 */
@RestController
@AllArgsConstructor
@Tag(name = "SysLogController", description = "系统日志")
@RequestMapping("/sys/log")
public class SysLogController extends AbstractController {
    private final SysLogService sysLogService;
    private final SysLoginLogService sysLoginLogService;

    /**
     * 列表
     */
    @GetMapping(value = "/list")
    @ResourceAuth(value = "系统日志列表", module = "系统日志")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper
                    .lambda()
                    .like(SysLog::getUsername, keyword)
                    .or()
                    .like(SysLog::getOperation, keyword);
        }
        queryWrapper.lambda().orderByDesc(SysLog::getCreateDate);
        IPage<SysLog> listPage = sysLogService.page(mpPageConvert.<SysLog>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }

    /**
     * 列表
     */
    @GetMapping(value = "/loginList")
    @ResourceAuth(value = "登录日志列表", module = "系统日志")
    public R loginList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper
                    .lambda()
                    .like(SysLoginLog::getUsername, keyword)
                    .or()
                    .like(SysLoginLog::getOptionName, keyword);
        }
        queryWrapper.lambda().orderByDesc(SysLoginLog::getOptionTime);
        IPage<SysLoginLog> listPage = sysLoginLogService.page(mpPageConvert.<SysLoginLog>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }

}