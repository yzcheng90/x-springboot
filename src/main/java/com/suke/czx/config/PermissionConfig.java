package com.suke.czx.config;

import cn.hutool.core.collection.CollUtil;
import com.suke.czx.authentication.role.PermissionEntity;
import com.suke.czx.common.annotation.ResourceAuth;
import com.suke.czx.common.utils.URLConvertUtil;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限收集
 */
@Slf4j
@Component
public class PermissionConfig implements InitializingBean {

    @Resource
    private WebApplicationContext applicationContext;

    @Getter
    @Setter
    private List<PermissionEntity> permissionEntities = new ArrayList<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        map.keySet().forEach(mappingInfo -> {
            HandlerMethod handlerMethod = map.get(mappingInfo);
            ResourceAuth method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResourceAuth.class);
            if (method != null) {
                PermissionEntity entity = new PermissionEntity();
                entity.setName(method.value());
                entity.setModuleName(method.module());
                if(mappingInfo.getPathPatternsCondition() != null){
                    mappingInfo.getPathPatternsCondition().getPatternValues().stream().forEach(url -> {
                        String strUrl = URLConvertUtil.capture(url);
                        String permission = URLConvertUtil.convert(url);
                        entity.setUrl(strUrl);
                        entity.setEnglishName(permission);
                        permissionEntities.add(entity);
                    });
                }
            }
        });
    }

    public PermissionEntity match(String url){
        List<PermissionEntity> entityList = permissionEntities
                .stream().filter(permission -> antPathMatcher.match(permission.getUrl(), url)).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(entityList)){
            return entityList.get(0);
        }else {
            return null;
        }
    }
}