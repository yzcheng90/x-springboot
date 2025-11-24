package com.suke.czx.modules.oss.cloud;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface ICloudStorage {

    default String newFileName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    default String newFileName(String suffix) {
        return UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
    }

    /**
     * 文件路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    default String getPath(String prefix, String suffix) {

        //文件路径
        String path = DateUtil.today() + "/" + newFileName();

        if (StrUtil.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }

        return path + suffix;
    }

    /**
     * 文件上传
     */
    String upload(MultipartFile file, String objectName);

    /**
     * 文件上传
     *
     * @param data 文件字节数组
     * @param path 文件路径，包含文件名
     * @return 返回http地址
     */
    String upload(byte[] data, String path);

    /**
     * 文件上传
     *
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return 返回http地址
     */
    String uploadSuffix(byte[] data, String suffix);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param path        文件路径，包含文件名
     * @return 返回http地址
     */
    String upload(InputStream inputStream, String path);

    /**
     * 文件上传
     *
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return 返回http地址
     */
    String uploadSuffix(InputStream inputStream, String suffix);

}
