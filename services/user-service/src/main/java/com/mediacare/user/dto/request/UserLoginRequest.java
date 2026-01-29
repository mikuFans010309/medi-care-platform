package com.mediacare.user.dto.request;

import com.mediacare.user.annotation.ValidaPassword;
import com.mediacare.user.annotation.ValidaPhone;
import lombok.Data;

//输入用户登录请求参数
@Data
public class UserLoginRequest {
    @ValidaPhone //自定义校验注解
    private String phone;      // 登录账号（手机号）
    @ValidaPassword
    private String password;   // 登录密码
}
