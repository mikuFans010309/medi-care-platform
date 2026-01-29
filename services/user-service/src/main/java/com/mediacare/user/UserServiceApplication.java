package com.mediacare.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 用户服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient // 启用服务注册发现
@MapperScan("com.mediacare.user.mapper") // 扫描 Mapper 接口
public class UserServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
