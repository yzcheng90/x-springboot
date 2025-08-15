package com.suke.czx.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.suke.czx.common.annotation.ShardingTable;
import com.suke.czx.common.lock.RedissonLock;
import com.suke.czx.common.utils.Constant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * 按月分表
 */
@Slf4j
@Configuration
public class ShardingTableConfig {

    private static final ThreadLocal<String> TABLE_NAME = new ThreadLocal<>();

    @Resource
    private RedissonLock redissonLock;

    public String shardingTable(String tableName) {
        // 判断是否包含分表
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (this.isShardingTable(tableInfo)) {
            String currentTableName = TABLE_NAME.get();
            if (StrUtil.isEmpty(currentTableName)) {
                currentTableName = this.getDefaultMonthTableName(tableName);
                log.info("使用分表:{}", currentTableName);
            }
            TABLE_NAME.remove();
            return currentTableName;
        }
        return tableName;
    }

    private String getDefaultMonthTableName(String tableName) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份是从0开始的
        return tableName + "_" + year + "_" + String.format("%02d", month);
    }

    private String getNextMonthTableName(String tableName) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 2; // 月份是从0开始的,查下个月的
        return tableName + "_" + year + "_" + String.format("%02d", month);
    }

    public static void setTableName(String tableName) {
        TABLE_NAME.set(tableName);
    }

    private boolean isShardingTable(TableInfo tableInfo) {
        if (tableInfo == null) {
            return false;
        }
        Class<?> entityType = tableInfo.getEntityType();
        ShardingTable shardingTable = entityType.getAnnotation(ShardingTable.class);
        return shardingTable != null;
    }

    /**
     * 每小时执行一次
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(cron = "1/5 * * * * ?")
    private void createShardingTable() {
        // 上锁30秒
        final boolean success = redissonLock.lock(Constant.SYSTEM_NAME + "createShardingTable", 30, TimeUnit.SECONDS);
        if (success) {
            log.info("开始检查下个月的分表情况...");
            // 创建分表
            TableInfoHelper.getTableInfos().forEach(tableInfo -> {
                if (isShardingTable(tableInfo)) {
                    String tableName = tableInfo.getTableName();
                    // 查询下个月的表是否创建好
                    String nextMonthTableName = getNextMonthTableName(tableName);
                    log.info("生成下个月的分表:{}", nextMonthTableName);
                    Object isExist = SqlRunner.db().selectObj("SHOW TABLES LIKE '" + nextMonthTableName + "';");
                    log.info("查询分表是否存在:{}", isExist == null);
                    if (isExist == null) {
                        // 创建新表
                        SqlRunner.db().update("CREATE TABLE " + nextMonthTableName + " LIKE " + tableName + ";");
                        log.info("开始创建分表..");
                    }
                }
            });
            log.info("检查完成");
        }
    }
}
