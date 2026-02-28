package com.mediacare.user.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class JwtUtil {
    // 1. 核心：使用 static final 保证不可变和线程安全
    private static SecretKey STATIC_SIGNING_KEY; // 直接存储密钥对象，而非字符串
    private static long STATIC_EXPIRATION;       // 单位：毫秒

    // 2. 临时接收 Spring 注入的值（非静态）
    private final String secretKey;
    private final int expiration;

    // 3. 构造器注入（推荐，比 @Value 更安全，强制依赖）
    public JwtUtil(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiration}") int expiration
    ) {
        this.secretKey = secretKey;
        this.expiration = expiration;

        // 4. 初始化静态常量（仅执行一次）
        initStaticFields();
    }



    //初始化静态字段，做校验和转换
    private void initStaticFields() {
        // 校验配置
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalArgumentException("jwt.secret-key 不能为空");
        }
        if (expiration <= 0) {
            throw new IllegalArgumentException("jwt.expiration 必须大于0");
        }

        // 一次性转换为 SecretKey，避免每次生成 Token 都重复转换（提升性能）
        STATIC_SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
        STATIC_EXPIRATION = expiration * 1000L; // 转换为毫秒

        System.out.println("JwtUtil 静态配置初始化完成，过期时间：" + STATIC_EXPIRATION + "ms");
    }
    // 生成 token
    public static String generateToken(Long userId, Integer userType) {
        // 构建 payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userType", userType);

        // 设置过期时间（2小时后）
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + STATIC_EXPIRATION* 1000L); // 2 hours

        // 生成 token
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, STATIC_SIGNING_KEY)
                .compact();
    }

    // 解析 token 获取 userId
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        //获取到的userId需要转换为Long
        return  claims.get("userId", Long.class);
    }

    // 解析 token 获取 userType
    public static Integer getUserTypeFromToken(String token) {
        Claims claims = parseClaims(token);
        return (Integer) claims.get("userType");
    }

    // 验证 token 是否有效
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(STATIC_SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
            //如果解析令牌出现问题，就说明令牌的不合法，就抛出异常
        } catch (Exception e) {
            return false;
        }
    }

    // 私有方法：解析 claims（私有方法是因为密钥不允许外部了解）
    private static Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(STATIC_SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
