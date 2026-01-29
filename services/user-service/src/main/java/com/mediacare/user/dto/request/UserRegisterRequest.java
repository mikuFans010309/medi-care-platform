package com.mediacare.user.dto.request;

import com.mediacare.user.annotation.ValidaPassword;
import com.mediacare.user.annotation.ValidaPhone;
import com.mediacare.user.annotation.ValidaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

//输入用户注册请求参数
@Data
public class UserRegisterRequest {

    @ValidaPhone //自定义手机号校验注解
    private String phone;           // 手机号（唯一标识）
    @ValidaPassword
    private String password;        // 明文密码（前端传入，后端加密存储）
    @NotBlank(message = "真实姓名不能为空")
    private String realName;        // 真实姓名
    @ValidaType
    private Integer userType;       // 用户类型：1-患者, 2-医生, 3-管理员
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{18}$", message = "身份证号格式错误")
    private String idCard;          // 身份证号（医生必填，用于资质验证）
}