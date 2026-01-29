package com.mediacare.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncodeConfig {


    // 密码编码器 PasswordEncoder是接口 BCryptPasswordEncoder是实现类
    //Bean 定义一个名为 passwordEncoder 的 Bean，供 Spring 管理（第三方Bean）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // cost factor = 10
    }
}
