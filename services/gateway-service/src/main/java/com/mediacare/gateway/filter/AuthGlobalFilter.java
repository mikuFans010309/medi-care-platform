package com.mediacare.gateway.filter;

import com.mediacare.gateway.Utils.JwtUtil;
import com.mediacare.gateway.enumPojo.ErrorCode;
import com.mediacare.gateway.exception.BusinessException;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


@Component // 注册过滤器
//GlobalFilter 接口，实现全局过滤器功能
//Ordered 接口，实现过滤器的执行顺序
public class AuthGlobalFilter implements GlobalFilter, Ordered {


    @Override
    /**
     * 过滤器的执行顺序，数字越小，优先级越高
     * @return
     */
    public int getOrder() {
        return -1;
    }

    // 从配置文件读取 JWT 密钥(因为jwtUtil以及读取了配置文件并且被ioc容易管理并初始化了，所以省略)


    // 白名单路径（无需鉴权）
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/sys/auth/login",
            "/sys/auth/register",
            "/error",           // Spring Boot 错误页面
            "/api/swagger-ui/**",   // Swagger 文档
            "/api/v3/api-docs/**"
    );

    @Override
    /**
     * 拦截请求，进行权限验证
     * @param exchange 请求对象（包含请求头、参数、路径等信息）
     * @param chain 过滤器链（继续执行下一个过滤器）
     * @return Mono<Void>
     */
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取请求路径
        String path = exchange.getRequest().getURI().getPath();

        // 2. 判断是否在白名单
        if (isExcludePath(path)) {
            return chain.filter(exchange);// 放行
        }

        // 3. 从 Header 获取 Token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        // 4. 验证 Token
        // 判断 Token 是否为空
        if (token == null || token.isEmpty()) {
            // Token 为空，返回错误(10004,无效的token)
            return Mono.error(BusinessException.off(ErrorCode.NOCARRY_AUTHORIZATION));
        }
        //因为获取到的token的字符串是"Bearer xxx" 所以需要截取(有空格的)
        token = token.substring(7);
        // 5. 解析 JWT（使用与 user-service 相同的工具逻辑）
        if (!JwtUtil.validateToken(token)){
            // Token 无效，返回错误状态码401
            return unauthorized(exchange, "无效的token");
        }

        // 6. 从 Token 提取用户信息
        Long userId = JwtUtil.getUserIdFromToken(token);
        Integer userType = JwtUtil.getUserTypeFromToken(token);

        // 7. 将用户信息写入 Request Header（透传给下游服务）
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId.toString())
                .header("X-User-Type", userType.toString())
                .build();

        // 8. 创建新的 Exchange 对象
        //???为什么不修改之前的exchange对象 因为Spring WebFlux 是响应式编程（Reactive），所有对象都是不可变的（Immutable）
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        //放行
        return chain.filter(newExchange);
    }

    // 判断是否为排除路径（支持通配符 /**）
    private boolean isExcludePath(String uri) {
        for (String exclude : EXCLUDE_PATHS) {
            //判断当前白名单路径是否以 "/**" 结尾。
            //例如：/swagger-ui/** 表示所有以 /swagger-ui/ 开头的路径都无需鉴权。
            if (exclude.endsWith("/**")) {
                //获取除去 /**结尾的前缀
                String prefix = exclude.substring(0, exclude.length() - 3);
                //判断uri请求路径是否以这个前缀开头
                if (uri.startsWith(prefix)) {
                    return true;
                }
                // 不是以/**结尾的，则直接判断是否相等
            } else if (uri.equals(exclude)) {
                return true;
            }
        }
        return false;
    }

    // 返回 401 错误
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        String body = "{\"code\":10004,\"msg\":\"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

}
