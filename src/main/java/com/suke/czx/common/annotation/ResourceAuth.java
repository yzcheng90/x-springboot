package com.suke.czx.common.annotation;

import java.lang.annotation.*;


/**
 *接口权限验证注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResourceAuth {
    /**
     * 接口名
     */
    String value() default "";

    /**
     * 模块
     */
    String module() default "";
}
