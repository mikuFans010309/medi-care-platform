package com.mediacare.user.util;


/*
    作用：清理当前线程的上下文，防止内存泄漏
    调用时机：在请求结束时（如 Filter 的 afterCompletion 或 Servlet 过滤器的 doFilter 结束）

    为什么用 ThreadLocal？
    在 Web 应用中，每次 HTTP 请求都由一个独立的线程处理。我们需要：
    在过滤器中解析 JWT，获取 userId
    将其传递给后续所有业务逻辑层（Service、Mapper）
    但又不能通过方法参数层层传递（太麻烦）
    所以使用 ThreadLocal 可以实现：
    “在一个请求生命周期内，所有代码都能访问到当前用户”
*/
public class UserContext {
    //私有防止实例化
    private UserContext() {}

    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<Integer> userTypeHolder = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    public static Long getUserId() {
        return userIdHolder.get();
    }

    public static void setUserType(Integer userType) {
        userTypeHolder.set(userType);
    }

    public static Integer getUserType() {
        return userTypeHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
        userTypeHolder.remove();
    }
}