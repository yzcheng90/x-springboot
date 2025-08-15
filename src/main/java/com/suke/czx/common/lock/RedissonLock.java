package com.suke.czx.common.lock;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 使用Redisson加锁
 */
@Slf4j
@Component
public class RedissonLock {

    @Resource
    private RedisLock redisLock;

    /**
     * Redission获取锁
     *
     * @param lockKey
     * @param delaySeconds
     * @param unit
     * @return
     */
    public boolean lock(String lockKey, long delaySeconds, final TimeUnit unit) {
        boolean success = false;
        try {
            success = redisLock.lock(lockKey, lockKey, delaySeconds, unit);
        } catch (Exception e) {
            log.error("[RedissonLock][lock]>>>> 加锁异常: {}", e.getMessage());
        }
        return success;
    }

    /**
     * Redission释放锁
     *
     * @param lockKey 锁名
     */
    public void unlock(String lockKey) {
        boolean status = redisLock.unlock(lockKey, lockKey);
        log.debug("[RedissonLock][unlock]>>>> status: {} ", status);
    }

}