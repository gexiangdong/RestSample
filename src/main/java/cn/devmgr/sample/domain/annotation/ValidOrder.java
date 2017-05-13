package cn.devmgr.sample.domain.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import cn.devmgr.sample.domain.validator.OrderValidator;

/**
 * 用于验证Order是否合法的注解；会调用OrderValidator去进行验证
 *
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = OrderValidator.class)
public @interface ValidOrder {
	String message() default "订单验证失败。";  
    Class<?>[] groups() default {};  
    Class<? extends Payload>[] payload() default {}; 
}
