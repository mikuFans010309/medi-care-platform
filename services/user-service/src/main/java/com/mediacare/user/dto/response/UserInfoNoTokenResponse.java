package com.mediacare.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//输出用户信息响应（不携带token）
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoNoTokenResponse {
    private Long id;              // 用户ID
    private String username;      // 登录账号（手机号）
    private String realName;      // 真实姓名
    private Integer userType;     // 用户类型：1-患者, 2-医生, 3-管理员
    private String avatarUrl;     // 头像URL（预留，暂无后期拓展OSS存储）
    private String phone;         // 手机号（与username一致，便于前端展示）
    private Integer status;       // 账户状态：1=启用, 0=禁用
}
