package com.suke.czx.common.lock;

import java.lang.annotation.*;

/**
 * 基于token
 * 防用户重复提交注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotDoubleSubmit {

    /**
     * 延时时间 在延时多久后可以再次提交,默认10秒
     * @return 秒
     */
    int delaySeconds() default 10;

}
