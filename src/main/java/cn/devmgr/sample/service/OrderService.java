package cn.devmgr.sample.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.devmgr.sample.dao.OrderDao;
import cn.devmgr.sample.domain.ConsigneeAddress;
import cn.devmgr.sample.domain.Order;
import cn.devmgr.sample.domain.OrderItem;

// 也可以使用JSR-107的cache注解；此处使用的是spring cache annotation
// CacheConfig设置了cacheNames后，此类的方法中的CacheEvict/CachePut/Cacheable等则不在需要设置名字，仅仅设置key（自动计算key）即可
@CacheConfig(cacheNames="order")
@Service
public class OrderService {
	private static final Log log = LogFactory.getLog(OrderService.class);

	@Resource
	private OrderDao orderDao;
	
	
	@Transactional
	public void append(Order order){
		orderDao.insertOrder(order);
	}
	
	
	/**
	 * key的表达式设置可以参照 Spring Cache SpEL (http://docs.spring.io/spring/docs/4.3.x/spring-framework-reference/html/cache.html#cache-spel-context)
	 */
	@CachePut(key="#result.id")
	@Transactional
	public Order getOneOrder(int orderId){

		Order newOrder = new Order();
		//newOrder.setConsignee("郝尤乾");
		newOrder.setOrderDate(new Date());
		newOrder.setConsigneeAddress(new ConsigneeAddress("张三", "13612348888", "江苏省", "南京市", "xx区", "xyz路20号"));
		List<OrderItem> list = new ArrayList<OrderItem>();
		Random rnd = new Random();
		int max = 1 + rnd.nextInt(5);
		for(int i=0; i<max; i++){
			OrderItem item = new OrderItem();
			item.setGiftId("EL" +  (rnd.nextInt() + 1000) % 1000);
			item.setGiftName("苹果" + (rnd.nextInt(20) + 1) + "代");
			item.setNum(rnd.nextInt(10) + 1);
			item.setJifen(rnd.nextInt(1000));
			item.setSupplyPrice(rnd.nextDouble() * 100);
			list.add(item);
		}
		newOrder.setOrderItems(list);
		
		int result = orderDao.insertOrder(newOrder);
		if(log.isTraceEnabled()){
			log.trace("插入的新记录" + newOrder.getId() + "; sql return " + result);
		}
		return newOrder;
	}
	
	@Cacheable(key="#id")
	public Order getOrder(int id){
		return orderDao.getOrderById(id);
	}
}
