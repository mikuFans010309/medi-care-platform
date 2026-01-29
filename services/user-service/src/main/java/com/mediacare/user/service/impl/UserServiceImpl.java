package com.mediacare.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediacare.user.dto.response.UserInfoNoTokenResponse;
import com.mediacare.user.dto.response.UserInfoResponse;
import com.mediacare.user.entity.SysUser;
import com.mediacare.user.enumPojo.ErrorCode;
import com.mediacare.user.exception.BusinessException;
import com.mediacare.user.service.UserService;
import com.mediacare.user.mapper.UserMapper;
import com.mediacare.user.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2026-01-26 15:56:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser>
    implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfoNoTokenResponse info() {
        //确保登陆过后有了token才能获取用户信息(获取id)
        Long userId= UserContext.getUserId();
        //根据id查询用户信息
        SysUser user = userMapper.selectById(userId);
        //判断用户信息是否为空
        if (user==null){
            throw  BusinessException.off(ErrorCode.USER_NOT_FOUND);
        }
        //封装成UserInfoResponse返回
        UserInfoNoTokenResponse userInfo= UserInfoNoTokenResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .userType(user.getUserType())
                .phone(user.getPhone())
                .status(user.getStatus())
                .build();
        return userInfo;
    }
}




