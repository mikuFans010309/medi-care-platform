package com.mediacare.user.dto.request;


import com.mediacare.user.annotation.ValidaPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// 输入更新个人资料请求参数
@Data
public class UpdateProfileRequest {
    @NotBlank(message = "真实姓名不能为空")
    private String realName;     // 真实姓名
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9xX]$", message = "身份证号格式不匹配")
    private String idCard;       // 身份证号（医生可修改）
    @ValidaPhone
    private String phone;        // 新手机号（若修改，需短信验证）
}
