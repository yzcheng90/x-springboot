package com.suke.czx.common.shardingtable;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.suke.czx.common.annotation.ShardingTable;
import com.suke.czx.common.lock.RedissonLock;
import com.suke.czx.common.utils.Constant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 按月分表
 */
@Slf4j
@Configuration
public class ShardingTableConfig implements TableNameHandler {

    private static final ThreadLocal<String> TABLE_NAME = new ThreadLocal<>();

    @Resource
    private RedissonLock redissonLock;

    /**
     * 根据表名，获取分表名称
     *
     * @param tableName
     * @return
     */
    @Override
    public String dynamicTableName(String sql, String tableName) {
        // 判断是否包含分表
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        if (this.isShardingTable(tableInfo)) {
            String currentTableName = TABLE_NAME.get();
            try {
                Statement parse = CCJSqlParserUtil.parse(sql);
                // 判断如果是查询类语句才用TABLE_NAME 中的表进行替换
                if (parse instanceof Select) {
                    // 如果传的的参数没有,则默认使用当前月
                    if (StrUtil.isEmpty(currentTableName)) {
                        currentTableName = this.getDefaultMonthTableName(tableName);
                    }
                    return currentTableName;
                } else {
                    // 增删改 都用最新的分表
                    return this.getDefaultMonthTableName(tableName);
                }
            } catch (Exception e) {
                log.info("SQL解析失败：{}", sql);
            }
        }
        return tableName;
    }

    /**
     * 生成当前月的表名
     *
     * @param tableName
     * @return
     */
    private String getDefaultMonthTableName(String tableName) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份是从0开始的
        return tableName + "_" + year + "_" + String.format("%02d", month);
    }

    /**
     * 生成下个月的表名
     *
     * @param tableName
     * @return
     */
    public String getNextMonthTableName(String tableName) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 2; // 月份是从0开始的,查下个月的
        if (month > 12) {
            year++;
            month = 1;
        }
        return tableName + "_" + year + "_" + String.format("%02d", month);
    }

    /**
     * 设置当前查询的表名
     *
     * @param tableName
     */
    public static void setTableName(String tableName) {
        TABLE_NAME.set(tableName);
    }

    /**
     * 清空当前分表
     */
    public static void removeTableName() {
        TABLE_NAME.remove();
    }

    /**
     * 查询是否有分表
     *
     * @param entityClass 对应表的实体类
     * @param date        时间
     * @return
     */
    public static String isExistShardingTable(Class<?> entityClass, String date) {
        try {
            String tableName = null;
            TableName tableNameAnnotation = entityClass.getAnnotation(TableName.class);
            if (tableNameAnnotation != null) {
                tableName = tableNameAnnotation.value();
            }
            if (StrUtil.isEmpty(tableName)) {
                return null;
            }
            DateTime parseDate = DateUtil.parse(date, "yyyy-MM");
            int year = DateUtil.year(parseDate);
            int month = DateUtil.month(parseDate) + 1;
            String table = tableName + "_" + year + "_" + String.format("%02d", month);
            Object isExist = SqlRunner.db().selectObj("SHOW TABLES LIKE '" + table + "';");
            if (isExist != null) {
                return table;
            }
        } catch (Exception e) {
            log.error("查询分表错误：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 判断是否是分表
     *
     * @param tableInfo
     * @return
     */
    public boolean isShardingTable(TableInfo tableInfo) {
        if (tableInfo == null) {
            return false;
        }
        Class<?> entityType = tableInfo.getEntityType();
        ShardingTable shardingTable = entityType.getAnnotation(ShardingTable.class);
        return shardingTable != null;
    }

    /**
     * 删除分表
     */
    public static void deleteShardingTable(String shardingTableName) {
        //模糊查询需删除的表集合
        List<Object> tables = SqlRunner.db().selectObjs("SHOW TABLES LIKE '%" + shardingTableName + "%';");
        if (tables != null) {
            String tableNamesStr = tables.stream().map(Object::toString).collect(Collectors.joining(","));
            // 删除表
            SqlRunner.db().update("DROP TABLE " + tableNamesStr + ";");
            log.info("开始删除分表..");
        }
    }

    /**
     * 创建分表
     *
     * @param shardingTableName
     * @param tableName
     */
    public void createShardingTable(String shardingTableName, String tableName) {
        Object isExist = SqlRunner.db().selectObj("SHOW TABLES LIKE '" + shardingTableName + "';");
        log.info("查询[{}]的分表[{}]是否存在:{}", tableName, shardingTableName, isExist != null);
        if (isExist == null) {
            // 创建新表
            SqlRunner.db().update("CREATE TABLE " + shardingTableName + " LIKE " + tableName + ";");
            log.info("开始创建分表..");
        }
    }

    /**
     * 服务启动后，延迟5秒执行
     * 检测当前月分表情况,如果不存在则创建
     */
    @Scheduled(initialDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void checkCurrMonthTable() {
        log.info("正在检测当前月的分表是否创建完成");
        // 上锁30秒
        final boolean success = redissonLock.lock(Constant.SYSTEM_NAME + "checkCurrMonthTable", 10, TimeUnit.SECONDS);
        if (success) {
            TableInfoHelper.getTableInfos().forEach(tableInfo -> {
                if (isShardingTable(tableInfo)) {
                    String tableName = tableInfo.getTableName();
                    String defaultMonthTableName = getDefaultMonthTableName(tableName);
                    createShardingTable(defaultMonthTableName, tableName);
                }
            });
        }
    }

}
