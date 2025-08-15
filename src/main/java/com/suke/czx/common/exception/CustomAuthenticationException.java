package com.suke.czx.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author czx
 * @title: CustomAuthenticationException
 * @projectName x-springboot
 * @date 2019/12/2416:45
 */
public class CustomAuthenticationException extends AuthenticationException {

    public CustomAuthenticationException(String msg){
        super(msg);
    }
}