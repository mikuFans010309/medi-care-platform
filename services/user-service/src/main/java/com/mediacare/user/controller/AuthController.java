package com.mediacare.user.controller;

import com.mediacare.user.dto.request.UserLoginRequest;
import com.mediacare.user.dto.request.UserRegisterRequest;
import com.mediacare.user.dto.response.UserInfoResponse;
import com.mediacare.user.entity.Result;

import com.mediacare.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 登录/注册
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@RequestBody @Validated UserLoginRequest request) {
        //1.接受请求参数，调用登录方法
        UserInfoResponse userInfo = authService.login(request);
        //2.返回登录结果，登录成功返回用户信息
        return Result.success(userInfo);
    }
    @PostMapping("/register")
    public Result register(@RequestBody @Validated UserRegisterRequest request) {
        //1.接受请求参数，调用注册方法
        authService.register(request);
        //2.返回注册结果，注册成功无返回值，状态码为200
        return Result.success();
    }
}
