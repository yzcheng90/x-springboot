package com.suke.czx.modules.apk.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;
import com.suke.czx.common.utils.R;
import com.suke.czx.modules.apk.entity.TbApkVersion;
import com.suke.czx.modules.apk.service.TbApkVersionService;
import com.suke.czx.modules.oss.cloud.OSSFactory;
import com.suke.czx.modules.oss.entity.SysOss;
import com.suke.czx.modules.oss.service.SysOssService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Map;


/**
 * APK版本管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-01-26 20:32:33
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/apk/version")
@Tag(name = "APK版本管理", description = "APK版本管理")
public class TbApkVersionController extends AbstractController {

    private final TbApkVersionService tbApkVersionService;
    private final SysOssService sysOssService;
    private final OSSFactory ossFactory;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ResourceAuth(value = "APK版本管理列表", module = "APK版本管理")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<TbApkVersion> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {
            queryWrapper.lambda().and(func -> func.like(TbApkVersion::getUpdateContent, keyword));
        }
        queryWrapper.lambda().orderByDesc(TbApkVersion::getCreateTime);
        IPage<TbApkVersion> listPage = tbApkVersionService.page(mpPageConvert.<TbApkVersion>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增APK版本管理
     */
    @SysLog("新增APK版本管理数据")
    @PostMapping("/save")
    @ResourceAuth(value = "新增APK版本管理数据", module = "APK版本管理")
    public R save(@RequestBody TbApkVersion param) {
        param.setCreateTime(new Date());
        param.setUserId(getUserId());
        tbApkVersionService.save(param);
        return R.ok();
    }

    /**
     * 上传文件
     */
    @AuthIgnore
    @PostMapping(value = "/upload/apk")
    public R uploadApk(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        if (prefix == null || !prefix.equals(".apk")) {
            return R.error("只能上传APK文件");
        }

        TbApkVersion apkVersion = null;
        File tempFile = new File(fileName);
        try {
            FileUtil.writeFromStream(file.getInputStream(), tempFile);
            ApkFile apkFile = new ApkFile(tempFile);
            ApkMeta apkMeta = apkFile.getApkMeta();
            apkVersion = new TbApkVersion();
            apkVersion.setVersionCode(Math.toIntExact(apkMeta.getVersionCode()));
            apkVersion.setVersionName(apkMeta.getVersionName());
            apkVersion.setAppName(apkMeta.getLabel());
            apkVersion.setPackageName(apkMeta.getPackageName());
            apkVersion.setFileName(file.getOriginalFilename());
            apkVersion.setMd5Value(DigestUtil.md5Hex(tempFile));
            apkVersion.setFileSize(String.valueOf(tempFile.length()));

            //上传文件
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String url = ossFactory.build().uploadSuffix(file.getBytes(), suffix);

            //保存文件信息
            SysOss ossEntity = new SysOss();
            ossEntity.setUrl(url);
            ossEntity.setCreateDate(new Date());
            sysOssService.save(ossEntity);
            apkVersion.setDownloadUrl(url);


        } catch (Exception e) {
            log.error("文件上传失败:{}", e.getMessage());
            return R.error("文件上传失败");
        }
        tempFile.delete();
        return R.ok().setData(apkVersion);
    }


    /**
     * 删除
     */
    @SysLog("删除APK版本管理数据")
    @PostMapping("/delete")
    @ResourceAuth(value = "删除APK版本管理数据", module = "APK版本管理")
    public R delete(@RequestBody TbApkVersion param) {
        if (param.getId() == null) {
            return R.error("ID为空");
        }
        tbApkVersionService.removeById(param.getId());
        return R.ok();
    }

}