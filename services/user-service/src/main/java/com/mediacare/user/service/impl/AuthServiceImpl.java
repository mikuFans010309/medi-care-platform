package com.mediacare.user.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediacare.user.dto.request.UserLoginRequest;
import com.mediacare.user.dto.request.UserRegisterRequest;
import com.mediacare.user.dto.response.UserInfoNoTokenResponse;
import com.mediacare.user.dto.response.UserInfoResponse;
import com.mediacare.user.entity.SysUser;
import com.mediacare.user.enumPojo.ErrorCode;
import com.mediacare.user.exception.BusinessException;
import com.mediacare.user.mapper.UserMapper;
import com.mediacare.user.service.AuthService;
import com.mediacare.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, SysUser>
        implements AuthService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 登录
     * @param request
     * @return
     */
    @Override
    public UserInfoResponse login(UserLoginRequest request) {
        // 1. 查询用户信息（返回 VO）
        SysUser user=userMapper.selectByLoginWithPwd(request);
        if (user==null){
            throw  BusinessException.off(ErrorCode.USER_NOT_FOUND);
        }
        // 2. 校验密码
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw BusinessException.off(ErrorCode.PASSWORD_ERROR);
        }
        // 3. 生成 Token
        String token = JwtUtil.generateToken(user.getId(), user.getUserType());
        // 4. 返回结果
        UserInfoResponse userInfo= UserInfoResponse.builder()
                .userInfo(UserInfoNoTokenResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .realName(user.getRealName())
                        .userType(user.getUserType())
                        .phone(user.getPhone())
                        .status(user.getStatus())
                        .build())
                .token(token)//下发 Token
                .build();
        return userInfo;
    }

    /**
     * 注册
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserRegisterRequest request) {
        SysUser sUser = userMapper.selectOne( //根据条件查询单行数据
                //添加eq等于条件，查询表中字段Phone的值是否有等于 request.getPhone()的
                new QueryWrapper<SysUser>().eq("phone", request.getPhone()));

        //判断手机号是否已存在
        if (sUser != null) {
            throw BusinessException.off(ErrorCode.PHONE_EXISTS);
        }

        SysUser user=SysUser.builder()
                .username(request.getPhone())//默认用户名使用手机号(方便管理控制)
                .password(passwordEncoder.encode(request.getPassword()))//密码存入数据库也需要加密
                .realName(request.getRealName())
                .userType(request.getUserType())
                .idCard(request.getIdCard())
                .phone(request.getPhone())
                .status(1)//注册成功时直接设置用户类型为启用
                .createdAt(DateTime.now())
                .updatedAt(DateTime.now())
                .build();
        userMapper.insert(user);
    }
}
