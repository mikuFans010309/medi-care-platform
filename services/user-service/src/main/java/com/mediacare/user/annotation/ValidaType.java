package com.mediacare.user.annotation;


import com.mediacare.user.annotation.Validator.userType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//此注解用于校验用户类型
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = userType.class)   //指定验证器类
public @interface ValidaType {
    // 必须添加的 message 属性
    String message() default "用户类型不合法（仅支持1-患者、2-医生、3-管理员）";

    // 必须添加的 groups 属性（用于分组校验）
    Class<?>[] groups() default {};

    // 必须添加的 payload 属性（用于携带额外校验元数据）
    Class<? extends Payload>[] payload() default {};
}
