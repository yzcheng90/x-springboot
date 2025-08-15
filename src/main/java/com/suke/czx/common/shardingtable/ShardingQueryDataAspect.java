package com.suke.czx.common.shardingtable;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.suke.czx.common.exception.RRException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 分表查询
 */
@Slf4j
@Aspect
@Component
public class ShardingQueryDataAspect {


    @Pointcut("@annotation(com.suke.czx.common.shardingtable.ShardingQueryData)")
    public void queryMethod() {
    }

    /**
     * 设置分表
     *
     * @param joinPoint
     */
    @Before("queryMethod()")
    public void beforeAdvice(JoinPoint joinPoint) {
        // 获取方法名称和参数
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof Map) {
                Map<String, Object> params = (Map<String, Object>) arg;
                String queryHistoryDate = MapUtil.getStr(params, "queryHistoryDate");
                log.info("queryDate:{}", queryHistoryDate);
                if (StrUtil.isNotEmpty(queryHistoryDate)) {
                    // 获取注解的 class 参数
                    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                    Method method = signature.getMethod();
                    ShardingQueryData entityTableClass = method.getAnnotation(ShardingQueryData.class);
                    Class<?> entityClass = entityTableClass.tableEntity();
                    // 设置分表
                    String shardingTable = ShardingTableConfig.isExistShardingTable(entityClass, queryHistoryDate);
                    if (StrUtil.isNotEmpty(shardingTable)) {
                        ShardingTableConfig.setTableName(shardingTable);
                    } else {
                        throw new RRException("暂无数据", 0);
                    }
                }
            }
        }
    }

    /**
     * 还原分表
     *
     * @param joinPoint
     */
    @After("queryMethod()")
    public void afterAdvice(JoinPoint joinPoint) {
        ShardingTableConfig.removeTableName();
    }
}