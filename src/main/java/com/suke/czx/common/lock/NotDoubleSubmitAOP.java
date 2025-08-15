package com.suke.czx.common.lock;

import cn.hutool.core.text.CharSequenceUtil;
import com.suke.czx.common.utils.Constant;
import com.suke.czx.common.utils.HttpContextUtils;
import com.suke.czx.common.utils.R;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 防重复提交注解的实现，使用AOP。
 */
@Slf4j
@Aspect
@Component
public class NotDoubleSubmitAOP {

    @Resource
    private RedissonLock redissonLock;

    @Around("execution(public * *(..)) && @annotation(NotDoubleSubmit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        // 获取到这个注解
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        NotDoubleSubmit notDoubleSubmit = method.getAnnotation(NotDoubleSubmit.class);

        /*
         * 锁 key
         */
        String token = HttpContextUtils.getHttpServletRequest().getHeader("token");
        final String lockKey = Constant.SYSTEM_NAME + generateKey(pjp, CharSequenceUtil.isEmpty(token) ? UUID.randomUUID().toString() : token);

        // 上锁
        final boolean success = redissonLock.lock(lockKey, notDoubleSubmit.delaySeconds(), TimeUnit.SECONDS);
        if (!success) {
            // 这里也可以改为自己项目自定义的异常抛出
            return R.error("操作太频繁");
        }
        return pjp.proceed();
    }

    private String generateKey(ProceedingJoinPoint pjp, String token) {
        StringBuilder sb = new StringBuilder();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        sb.append(token);
        sb.append(pjp.getTarget().getClass().getName()).append(method.getName());//方法名
        for (Object o : pjp.getArgs()) {
            sb.append(o.toString());
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes(Charset.defaultCharset()));
    }

}