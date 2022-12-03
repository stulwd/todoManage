package org.sleekflow.lwd.config.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan({"org.sleekflow.lwd.mapper"})
public class MyBatisConfig {

}
