package com.mediacare.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.List;

/**
 * Sentinel 网关配置 - 简化版本
 * 主要注册过滤器，限流逻辑在 RateLimitFilter 中实现
 */
@Slf4j
@Configuration
public class SimpleSentinelConfig {

    // 注册 Sentinel 异常处理器
    @Bean
    @Order(-1)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(
            List<ViewResolver> viewResolvers,
            ServerCodecConfigurer serverCodecConfigurer
    ) {
        log.info("注册 Sentinel 异常处理器");
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    // 注册 Sentinel 过滤器
    @Bean
    @Order(-2)
    public GlobalFilter sentinelGatewayFilter() {
        log.info("注册 Sentinel 过滤器");
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void init() {
        log.info("Sentinel 网关配置初始化完成");
    }
}