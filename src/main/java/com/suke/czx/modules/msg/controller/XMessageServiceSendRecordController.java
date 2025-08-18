package com.suke.czx.modules.msg.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.shardingtable.ShardingQueryData;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.msg.entity.XMessageServiceSendRecord;
import com.suke.czx.modules.msg.service.XMessageServiceSendRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * 短信发送记录（按月分表）
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2025-08-15 17:40:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/msg/record")
@Tag(name = "XMessageServiceSendRecordController", description = "短信发送记录（按月分表）")
public class XMessageServiceSendRecordController extends AbstractController {
    private final XMessageServiceSendRecordService xMessageServiceSendRecordService;

    /**
     * 列表
     */
    @ShardingQueryData(tableEntity = XMessageServiceSendRecord.class)
    @GetMapping("/list")
    @ResourceAuth(value = "短信发送记录（按月分表）列表", module = "短信发送记录（按月分表）")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<XMessageServiceSendRecord> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(func -> {
                func.like(XMessageServiceSendRecord::getSendContent, keyword).or().eq(XMessageServiceSendRecord::getSendMobile, keyword);
            });
        }

        String tenancyId = MapUtil.getStr(params, "tenancyId");
        if (StrUtil.isNotEmpty(tenancyId)) {
            queryWrapper.lambda().eq(XMessageServiceSendRecord::getTenancyId, tenancyId);
        }

        IPage<XMessageServiceSendRecord> listPage = xMessageServiceSendRecordService.page(mpPageConvert.<XMessageServiceSendRecord>pageParamConvert(params), queryWrapper);
        this.fullTenancyInfo(listPage);
        return R.ok().setData(listPage);
    }

}