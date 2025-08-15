package com.suke.czx.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by czx on 2021/04/02.
 */
@ComponentScan(basePackages = {"com.suke.czx.modules"})
@Configuration
public class SwaggerConfig {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(swaggerProperties.getTitle())
                        .contact(new Contact())
                        .description(swaggerProperties.getDescription())
                        .version(swaggerProperties.getVersion())
                        .license(new License().name(swaggerProperties.getAuthor()).url(swaggerProperties.getUrl())))
                .externalDocs(new ExternalDocumentation()
                        .description(swaggerProperties.getEmail())
                        .url(swaggerProperties.getUrl()));
    }


}