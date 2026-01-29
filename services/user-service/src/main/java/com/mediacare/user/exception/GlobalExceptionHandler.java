package com.mediacare.user.exception;

import com.mediacare.user.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理Service业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusiness(BusinessException ex) {
        log.error("业务异常: code:{},message:{}", ex.getCode(),ex.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    //处理其他异常（兜底）
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        log.error("系统异常:", ex);
        return Result.error(500, "系统异常");
    }
}
