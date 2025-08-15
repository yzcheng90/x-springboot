package com.suke.czx.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement
@MapperScan(value = "com.suke.czx.modules.*.mapper")
public class MyBatisPlusConfig {

    @Resource
    public ShardingTableConfig shardingTableHandler;

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 拦截器
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 动态表名插件
        DynamicTableNameInnerInterceptor dynamicTableNameInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInterceptor.setTableNameHandler(((sql, tableName) -> shardingTableHandler.shardingTable(tableName)));
        interceptor.addInnerInterceptor(dynamicTableNameInterceptor);
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        return interceptor;
    }
}