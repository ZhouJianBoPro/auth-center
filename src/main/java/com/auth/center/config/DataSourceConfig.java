package com.auth.center.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Desc: 配置seata代理数据源，用于拦截数据源的sql
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/30 14:20
 **/
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties) {
        
        // 使用HikariDataSource初始化数据源。
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        
        // 使用Seata的DataSourceProxy代理数据源
        return new DataSourceProxy(dataSource);
    }
}
