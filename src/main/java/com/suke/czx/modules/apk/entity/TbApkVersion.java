package com.suke.czx.modules.apk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * APK版本管理
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2023-01-26 20:32:33
 */
@Data
@TableName("tb_apk_version")
public class TbApkVersion implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "")
    @JsonProperty(value = "id")
    public Long id;

    @Schema(description = "更新内容")
    @JsonProperty(value = "updateContent")
    public String updateContent;

    @Schema(description = "版本码")
    @JsonProperty(value = "versionCode")
    public Integer versionCode;

    @Schema(description = "版本号")
    @JsonProperty(value = "versionName")
    public String versionName;

    @Schema(description = "包名")
    @JsonProperty(value = "packageName")
    public String packageName;

    @Schema(description = "下载地址")
    @JsonProperty(value = "downloadUrl")
    public String downloadUrl;

    @Schema(description = "APP名")
    @JsonProperty(value = "appName")
    public String appName;

    @Schema(description = "MD5值")
    @JsonProperty(value = "md5Value")
    public String md5Value;

    @Schema(description = "文件名")
    @JsonProperty(value = "fileName")
    public String fileName;

    @Schema(description = "文件大小")
    @JsonProperty(value = "fileSize")
    public String fileSize;

    @Schema(description = "是否强制安装")
    @JsonProperty(value = "isForce")
    public Integer isForce;

    @Schema(description = "是否可忽略该版本")
    @JsonProperty(value = "isIgnorable")
    public Integer isIgnorable;

    @Schema(description = "是否静默下载")
    @JsonProperty(value = "isSilent")
    public Integer isSilent;

    @Schema(description = "上传人")
    @JsonProperty(value = "userId")
    public String userId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createTime")
    public Date createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "updateTime")
    public Date updateTime;

}