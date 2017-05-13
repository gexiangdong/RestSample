package cn.devmgr.sample.domain.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.devmgr.sample.domain.Order;
import cn.devmgr.sample.domain.OrderItem;
import cn.devmgr.sample.domain.annotation.ValidOrder;

/**
 * 
 * 校验Order类数据是否合法的例子
 *
 */
public class OrderValidator implements ConstraintValidator<ValidOrder, Order> {
	private final static Log log = LogFactory.getLog(OrderValidator.class);

	@Override
	public void initialize(ValidOrder order) {
		if(log.isTraceEnabled()){
			log.trace("validate ... " + order);
		}
	}

	@Override
	public boolean isValid(Order order, ConstraintValidatorContext context) {
		if(log.isTraceEnabled()){
			log.trace("isValid ... " + order + " " + order.getConsigneeAddress());
		}
		int cnt = 0;
		for(OrderItem item : order.getOrderItems()){
			cnt += item.getNum();
		}
		if(cnt > 50){
			 context.disableDefaultConstraintViolation();
		     context.buildConstraintViolationWithTemplate("订购商品总数不能超过50").addConstraintViolation();
		     return false;
		}
		return true;
	}

}
