package com.mediacare.user.service;

import com.mediacare.user.dto.response.UserInfoNoTokenResponse;
import com.mediacare.user.dto.response.UserInfoResponse;
import com.mediacare.user.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2026-01-26 15:56:39
*/
public interface UserService extends IService<SysUser> {

    UserInfoNoTokenResponse info();
}
