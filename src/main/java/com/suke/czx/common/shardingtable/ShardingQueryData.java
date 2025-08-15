package com.suke.czx.common.shardingtable;

import java.lang.annotation.*;

/**
 * 分表查询注解
 * 只支持 @RequestParam Map<String, Object> params 这种参数类型
 */
@Documented
@Target(ElementType.METHOD) // 表示注解可以应用于方法
@Retention(RetentionPolicy.RUNTIME) // 表示注解在运行时可用
public @interface ShardingQueryData {

    Class<?> tableEntity();
}