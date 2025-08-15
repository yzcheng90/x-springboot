package com.suke.czx.modules.gen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.modules.gen.entity.InfoRmationSchema;
import com.suke.czx.modules.gen.entity.MakerConfigEntity;
import com.suke.czx.modules.gen.service.SysGenService;
import com.suke.czx.common.utils.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/sys/gen")
@AllArgsConstructor
@Tag(name = "SysGenController", description = "代码生成")
public class SysGenController extends AbstractController {

    private final SysGenService sysGenService;

    /**
     * 列表
     */
    @GetMapping(value = "/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<InfoRmationSchema> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper
                    .lambda()
                    .like(InfoRmationSchema::getTableName, keyword);
        }
        IPage<InfoRmationSchema> pageList = sysGenService.queryTableList(mpPageConvert.<InfoRmationSchema>pageParamConvert(params), queryWrapper);
        return R.ok().setData(pageList);
    }

    /**
     * 生成代码
     *
     * @param makerConfigEntity
     */
    @PostMapping(value = "/create")
    public R create(@RequestBody MakerConfigEntity makerConfigEntity) {
        sysGenService.generatorCode(makerConfigEntity);
        return R.ok();
    }
}