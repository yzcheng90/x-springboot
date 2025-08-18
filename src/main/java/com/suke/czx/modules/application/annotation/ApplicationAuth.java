package com.suke.czx.modules.application.annotation;

import com.suke.czx.common.annotation.AuthIgnore;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthIgnore
public @interface ApplicationAuth {
}
