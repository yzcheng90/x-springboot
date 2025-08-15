package com.suke.czx.modules.oss.cloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 16:38
 * @Author yzcheng90@qq.com
 **/
@Data
@Component
@ConfigurationProperties(value = "qiniu")
public class QiniuProperties {

    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String domain;
    private String prefix;

}
