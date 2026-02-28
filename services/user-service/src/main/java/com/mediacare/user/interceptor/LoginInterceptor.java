
package com.mediacare.user.interceptor;


import com.mediacare.user.util.JwtUtil;
import com.mediacare.user.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从网关透传的 Header 中获取用户信息（由 gateway-service 设置）
        String userIdHeader = request.getHeader("X-User-Id");
        String userTypeHeader = request.getHeader("X-User-Type");
        // 2. 判断是否为空（理论上不该为空，因为网关已鉴权）
        if (userIdHeader == null || userIdHeader.trim().isEmpty()){
            writeErrorResponse(response, "非法请求：缺少用户上下文");
            log.warn("请求未携带 X-User-Id，可能绕过网关。URL: {}, IP: {}",
                    request.getRequestURI(), request.getRemoteAddr());
            return false;
        }
        // 3. 转换为 Long / Integer
        Long userId;
        Integer userType;
        try {
            userId = Long.valueOf(userIdHeader);
            userType = Integer.valueOf(userTypeHeader);
        } catch (NumberFormatException e) {
            writeErrorResponse(response, "用户上下文格式错误");
            log.error("X-User-Id 或 X-User-Type 格式无效: userId={}, userType={}",
                    userIdHeader, userTypeHeader);
            return false;
        }
        // 4. 放入线程上下文（供 Service 层使用）
        UserContext.setUserId(userId);
        UserContext.setUserType(userType);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在请求完成时清理上下文，避免内存泄漏。
        UserContext.clear();
    }

    //错误响应方法
    private void writeErrorResponse(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(401);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\"}");
    }

}

