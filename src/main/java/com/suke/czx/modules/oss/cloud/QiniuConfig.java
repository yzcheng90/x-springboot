package com.suke.czx.modules.oss.cloud;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Date 16:41
 * @Author yzcheng90@qq.com
 **/
@Configuration
@AllArgsConstructor
public class QiniuConfig {

    private final QiniuProperties qiniuProperties;

    @Bean
    public Auth getAuth() {
        Auth auth = Auth.create(qiniuProperties.getAccessKey(), qiniuProperties.getSecretKey());
        return auth;
    }

    @Bean
    public UploadManager getUploadManager() {
        return new UploadManager(new com.qiniu.storage.Configuration());
    }

}
