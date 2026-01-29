package com.mediacare.user.annotation;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "手机号不能为空")
//因为直接使用正则表达式，所以不需要自定义验证器，所以也不需要添加 @Constraint和message和groups属性
@Pattern(regexp = "^1[3-9]\\d{9} $ ", message = "手机号格式不正确") // 简单的手机号正则
public @interface ValidaPhone {
    
}
