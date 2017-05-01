package cn.devmgr.sample.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.devmgr.sample.dao.mapper.OrderDao;
import cn.devmgr.sample.domain.ConsigneeAddress;
import cn.devmgr.sample.domain.Order;
import cn.devmgr.sample.domain.OrderItem;

@Service
public class OrderService {
	private static final Log log = LogFactory.getLog(OrderService.class);

	@Autowired
	private OrderDao orderDao;


	
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
	
	public Order getOrder(int id){
		return orderDao.getOrderById(id);
	}
}
