package com.suke.czx.config;

import cn.hutool.core.util.ReUtil;
import com.suke.czx.common.annotation.AuthIgnore;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author czx
 * @title: AuthIgnoreConfig
 * @projectName x-springboot
 * @description: 忽略权限认证
 * @date 2019/12/2415:56
 */
@Slf4j
@Configuration
public class AuthIgnoreConfig implements InitializingBean {

    @Resource
    private WebApplicationContext applicationContext;

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
    private static final String ASTERISK = "*";

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Getter
    @Setter
    private List<String> ignoreUrls = new ArrayList<>();

    @Override
    public void afterPropertiesSet(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(mappingInfo -> {
            HandlerMethod handlerMethod = map.get(mappingInfo);
            AuthIgnore method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AuthIgnore.class);
            if(method != null){
                PathPatternsRequestCondition pathPatternsCondition = mappingInfo.getPathPatternsCondition();
                if(pathPatternsCondition != null){
                    pathPatternsCondition.getPatterns().forEach(url ->{
                        String patternString = url.getPatternString();
                        ignoreUrls.add(ReUtil.replaceAll(patternString, PATTERN, ASTERISK));
                    });
                }
            }
        });
    }

    public boolean isContains(String url) {
        final String u = ReUtil.replaceAll(url, PATTERN, ASTERISK);
        return ignoreUrls.contains(u);
    }

    public boolean match(String url) {
        long count = ignoreUrls.stream().filter(u -> antPathMatcher.match(u, url)).count();
        return count > 0;
    }
}