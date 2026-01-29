package com.mediacare.user.annotation;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "密码不能为空")
//6~15位数字或者字符或者符号组成的密码
@Pattern(regexp = "^(?=.{6,15}$)[\\p{L}\\p{N}\\p{P}\\p{S}]+$", message = "密码格式不匹配")
public @interface ValidaPassword {
}
