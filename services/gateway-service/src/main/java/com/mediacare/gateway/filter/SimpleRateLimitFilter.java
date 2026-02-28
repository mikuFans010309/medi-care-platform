package com.mediacare.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义限流过滤器 - 替代 Sentinel 限流
 * 使用简单的计数器算法实现限流功能
 */
@Slf4j
@Component
@Order(-1) // 最高优先级
public class SimpleRateLimitFilter implements GlobalFilter {

    // 存储每个路径的访问计数器
    private final Map<String, PathRateLimiter> rateLimiters = new ConcurrentHashMap<>();

    // 限流配置
    private static final int MAX_REQUESTS_PER_SECOND = 2;
    private static final Duration TIME_WINDOW = Duration.ofSeconds(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // 只对认证相关路径进行限流
        if (!shouldRateLimit(path)) {
            return chain.filter(exchange);
        }

        // 使用路径作为限流键
        String rateLimitKey = path;
        PathRateLimiter rateLimiter = rateLimiters.computeIfAbsent(rateLimitKey, 
            k -> new PathRateLimiter(MAX_REQUESTS_PER_SECOND, TIME_WINDOW));

        if (rateLimiter.tryAcquire()) {
            log.debug("请求通过限流: path={}", path);
            return chain.filter(exchange);
        } else {
            log.warn("请求被限流: path={}", path);
            return handleRateLimitExceeded(exchange);
        }
    }

    private boolean shouldRateLimit(String path) {
        // 只对认证相关路径进行限流
        return path.contains("/auth/login") || path.contains("/auth/register");
    }

    private Mono<Void> handleRateLimitExceeded(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String json = "{\"code\":50001,\"msg\":\"请求过于频繁，请稍后再试\",\"data\":null}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    /**
     * 简单的路径限流器
     */
    private static class PathRateLimiter {
        private final int maxRequests;
        private final Duration timeWindow;
        private final AtomicInteger requestCount = new AtomicInteger(0);
        private volatile Instant windowStart = Instant.now();

        public PathRateLimiter(int maxRequests, Duration timeWindow) {
            this.maxRequests = maxRequests;
            this.timeWindow = timeWindow;
        }

        public synchronized boolean tryAcquire() {
            Instant now = Instant.now();
            
            // 检查是否超出时间窗口
            if (Duration.between(windowStart, now).compareTo(timeWindow) > 0) {
                windowStart = now;
                requestCount.set(0);
            }

            int currentCount = requestCount.get();
            if (currentCount < maxRequests) {
                requestCount.incrementAndGet();
                return true;
            }
            
            return false;
        }
    }
}