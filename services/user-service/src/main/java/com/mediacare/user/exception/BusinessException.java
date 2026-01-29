package com.mediacare.user.exception;

import com.mediacare.user.enumPojo.ErrorCode;

import java.io.Serial;

public class BusinessException extends RuntimeException{
    // @Serial的作用是标识参与序列化的字段，保证字段的唯一性。
    @Serial
    private static final long serialVersionUID = 1L;

    public BusinessException(Integer code, String message){
        super(message);
        this.code = code;
    }

    //错误码
    private Integer code;

    public Integer getCode() {
        return code;
    }

    //打印当前异常的信息
    @Override
    public String toString() {
        return  "BusinessException{" +
                "code=" + code +
                ", message=" + super.getMessage() +
                '}';
    }

    //简单的封装，对异常设置错误码进行封装
    public static BusinessException off(ErrorCode code) {
        return new BusinessException(code.getCode(), code.getMessage());
    }
}
