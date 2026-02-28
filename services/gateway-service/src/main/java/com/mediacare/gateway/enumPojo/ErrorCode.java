package com.mediacare.gateway.enumPojo;


// 错误码枚举
public enum ErrorCode {

    //定义用户模块功能错误码
    PHONE_EXISTS(10001, "手机号已注册"),
    USER_NOT_FOUND(10002, "用户不存在"),
    PASSWORD_ERROR(10003, "密码错误"),
    INVALID_TOKEN(10004, "无效的Token"),
    USER_TYPE_NOT_ALLOWED(10005, "用户类型不允许"),
    NOCARRY_AUTHORIZATION(10006, "未携带Authorization(/token)");

    ErrorCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}

