package com.mediacare.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//输出用户信息响应(携带token)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private UserInfoNoTokenResponse userInfo;
    private String token;         // 登录Token 此表变量便于后端查看以及测试无需真实返还前端
}
