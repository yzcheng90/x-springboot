package com.suke.czx.modules.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.exception.RRException;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.oss.entity.SysOss;
import com.suke.czx.modules.oss.service.SysOssService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 文件上传
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2017-03-25 12:13:26
 */
@Slf4j
@RestController
@RequestMapping("sys/oss")
@AllArgsConstructor
@Tag(name = "SysOssController", description = "文件上传")
public class SysOssController extends AbstractController {

    private final SysOssService sysOssService;

    /**
     * 列表
     */
    @GetMapping(value = "/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<SysOss> queryWrapper = new QueryWrapper<>();
        IPage<SysOss> pageList = sysOssService.page(mpPageConvert.<SysOss>pageParamConvert(params), queryWrapper);
        return R.ok().setData(pageList);
    }

    /**
     * 上传文件
     */
    @PostMapping(value = "/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }

        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String url = "";

        //保存文件信息
        SysOss ossEntity = new SysOss();
        ossEntity.setUrl(url);
        ossEntity.setCreateDate(new Date());
        sysOssService.save(ossEntity);

        return R.ok().put("url", url);
    }


    /**
     * 删除
     */
    @PostMapping(value = "/delete")
    public R delete(@RequestBody Long[] ids) {
        sysOssService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}