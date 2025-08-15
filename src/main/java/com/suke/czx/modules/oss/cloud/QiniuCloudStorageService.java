package com.suke.czx.modules.oss.cloud;

import cn.hutool.core.io.IoUtil;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.suke.czx.common.exception.RRException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 七牛云存储
 *
 * @author czx
 * @email object_czx@163.com
 * @date 2017-03-25 15:41
 */
@Component
@AllArgsConstructor
public class QiniuCloudStorageService implements ICloudStorage {

    private final Auth auth;
    private final UploadManager uploadManager;
    private final QiniuProperties qiniuProperties;

    @Override
    public String upload(byte[] data, String path) {
        try {
            String token = auth.uploadToken(qiniuProperties.getBucketName());
            Response res = uploadManager.put(data, path, token);
            if (!res.isOK()) {
                throw new RRException("上传七牛出错：" + res.getInfo());
            }
        } catch (Exception e) {
            throw new RRException("上传文件失败，请核对七牛配置信息", e);
        }

        return qiniuProperties.getDomain() + "/" + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        byte[] data = IoUtil.readBytes(inputStream);
        return this.upload(data, path);
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(qiniuProperties.getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(qiniuProperties.getPrefix(), suffix));
    }
}