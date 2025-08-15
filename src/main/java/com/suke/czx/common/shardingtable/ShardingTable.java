package com.suke.czx.common.shardingtable;

import java.lang.annotation.*;

/**
 * 分表注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface ShardingTable {
}