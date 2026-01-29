package com.mediacare.user.mapper;

import com.mediacare.user.dto.request.UserLoginRequest;
import com.mediacare.user.dto.response.UserInfoResponse;
import com.mediacare.user.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2026-01-26 15:56:39
* @Entity com.mediacare.user.entity.SysUser
*/
public interface UserMapper extends BaseMapper<SysUser> {

    @Select("select id,username,password,real_name,id_card,phone,user_type,status,created_at,updated_at from sys_user " +
            "where phone=#{phone} ;")
    SysUser selectByLoginWithPwd(UserLoginRequest request);
}




