package com.suke.czx.modules.oss.cloud;

import com.suke.czx.common.exception.RRException;
import com.suke.czx.modules.oss.entity.SysOssSetting;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @Author czx
 * @Description minio 文件存储 具体其他操作看官网 https://docs.min.io/
 **/
@Slf4j
public class MinioCloudStorageService implements ICloudStorage {

    private final MinioClient client;
    private final SysOssSetting config;


    public MinioCloudStorageService(SysOssSetting config) {
        this.config = config;
        try {
            //  使用 MinioClient 的构建器模式创建一个 MinioClient 实例
            client = MinioClient.builder()
                    //设置 Minio 服务的端点地址
                    .endpoint(config.getUrl())
                    // 设置访问 Minio 服务所需的访问密钥和秘密密钥
                    .credentials(config.getAccessKey(), config.getSecretKey())
                    // 构建并返回 MinioClient 实例
                    .build();
        } catch (Exception e) {
            log.error("minio初始化失败：{}", e.getMessage());
            throw new RRException("minio初始化失败");
        }
    }

    @Override
    public String upload(MultipartFile file, String path) {
        try {
            InputStream inputStream = file.getInputStream(); // 获取文件的输入流，以便上传
            client.putObject(// 调用 MinIO 客户端的 putObject 方法，上传文件
                    PutObjectArgs.builder()// 构建 PutObjectArgs 对象
                            .bucket(config.getBucketName())// 设置桶名
                            .object(path)// 设置对象名
                            .contentType(file.getContentType())// 设置文件的内容类型
                            .stream(inputStream, inputStream.available(), -1) // 设置输入流及其可用字节数，-1 表示不限制
                            .build());// 构建参数并调用 putObject
            return config.getView() + "/" + config.getBucketName() + "/" + path;
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage());
            throw new RRException("上传文件失败", e);
        }
    }


    @Override
    public String upload(byte[] data, String path) {
        return upload(new ByteArrayInputStream(data), path);
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, newFileName(suffix));
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            client.putObject( // 调用 MinIO 客户端的 putObject 方法进行文件上传
                    PutObjectArgs.builder()// 使用 PutObjectArgs 构建上传参数
                            .bucket(config.getBucketName())// 设置存储桶名称
                            .object(path)// 设置对象名称（在存储桶中的文件名）
                            .stream(inputStream, inputStream.available(), -1)// 设置输入流及其大小，-1 表示使用默认值
                            .build()); // 构建并返回 PutObjectArgs 对象，然后执行上传
            return config.getView() + "/" + config.getBucketName() + "/" + path;
        } catch (Exception e) {
            throw new RRException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, newFileName(suffix));
    }

}
