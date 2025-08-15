package com.suke.czx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 16:01
 * @Author yzcheng90@qq.com
 **/
@Data
@Component
@ConfigurationProperties(value = "swagger")
public class SwaggerProperties {

    /**
     * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
     */
    private Boolean enable = false;

    /**
     * 项目信息
     */
    private String title = "";

    /**
     * 描述信息
     */
    private String description = "";

    /**
     * 版本信息
     */
    private String version = "";

    /**
     * 作者
     */
    private String author = "";

    /**
     * url
     */
    private String url = "";

    /**
     * email
     */
    private String email = "";

}
