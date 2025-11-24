package com.suke.czx.modules.oss.cloud;

import cn.hutool.core.util.StrUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.suke.czx.common.exception.RRException;
import com.suke.czx.modules.oss.entity.SysOssSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 七牛云存储
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2017-03-25 15:41
 */
@Slf4j
public class QiniuCloudStorageService implements ICloudStorage {

    private final Auth auth;
    private final UploadManager uploadManager;
    private final SysOssSetting config;

    public QiniuCloudStorageService(SysOssSetting config) {
        this.config = config;
        auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        uploadManager = new UploadManager(new com.qiniu.storage.Configuration());
    }

    @Override
    public String upload(MultipartFile file, String objectName) {
        try {
            if (StrUtil.isNotEmpty(config.getPrefix())) {
                objectName = "uploads/" + config.getPrefix() + "/" + objectName;
            } else {
                objectName = "uploads/" + objectName;
            }
            return upload(file.getBytes(), objectName);
        } catch (Exception e) {
            throw new RRException("上传七牛出错：" + e.getMessage());
        }
    }

    @Override
    public String upload(byte[] data, String path) {
        try {
            String token = auth.uploadToken(config.getBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RRException("上传七牛出错：" + res.getInfo());
            }
        } catch (Exception e) {
            throw new RRException("上传文件失败，请核对七牛配置信息", e);
        }

        return config.getView() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RRException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getPrefix(), suffix));
    }
}