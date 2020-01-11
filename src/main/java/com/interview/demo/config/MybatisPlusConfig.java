package com.interview.demo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: interviewDemo
 * @Package: com.interview.demo.config
 * @ClassName: MybatisPlusConfig
 * @Author: Kaiser
 * @Description: mybatis配置类
 * @Date: 2020-01-10 10:05
 * @Version: 1.0
 */
@Configuration
@MapperScan("com.interview.demo.mapper*")
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
