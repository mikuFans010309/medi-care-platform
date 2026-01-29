package com.mediacare.user.annotation.Validator;

import com.mediacare.user.annotation.ValidaType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * UserType 注解的验证器实现类  一定要实现 ConstraintValidator 接口
 * <p>
 * 逻辑：检查 Integer 是否等于 1、2 或 3
 */
public class userType implements ConstraintValidator<ValidaType, Integer> {
    /**
     * 初始化方法
     *此校验不需要初始化，所以直接调用父类方法
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ValidaType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        // 可以在这里处理注解参数，比如 message
    }

    /**
     * 验证逻辑
     *
     * @param integer 自定义校验器通过这个参数获取需要验证的具体数值
     * @param constraintValidatorContext 验证器上下文（比如被校验字段的名称、注解属性等）
     * @return
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        if (integer == null) {
            return true; //可以为空所以返回true
        }
        return integer == 1 || integer == 2 || integer == 3;
    }
}
