package com.mediacare.user.controller;

import com.mediacare.user.dto.response.UserInfoNoTokenResponse;
import com.mediacare.user.entity.Result;
import com.mediacare.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//用户信息管理(需要登录)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/info")
    public Result info(){
        //无参数，但是请求头需要携带  Authorization: Bearer <token>
        UserInfoNoTokenResponse userInfo = userService.info();
        return Result.success(userInfo);
    }
}
