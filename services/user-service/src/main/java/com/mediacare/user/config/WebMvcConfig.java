package com.mediacare.user.config;

import com.mediacare.user.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns( "/auth/login",
                        "/auth/register",
                        "/error",           // Spring Boot 错误页面
                        "/swagger-ui/**",   // Swagger 文档
                        "/v3/api-docs/**",
                        "/actuator/health");  // 健康检查
    }
}
