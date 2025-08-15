package com.suke.czx.common.annotation;

import java.lang.annotation.*;

/**
 * 分表注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ShardingTable {
}