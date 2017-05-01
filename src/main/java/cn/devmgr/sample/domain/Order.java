package cn.devmgr.sample.domain;

import java.util.Date;
import java.util.List;

public class Order {
	private int id;
	private Date orderDate;
	private ConsigneeAddress consigneeAddress;
	private List<OrderItem> orderItems;
	private int status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public int getTotalJifen(){
		if(this.orderItems == null){
			throw new RuntimeException("no orderItems found in order #" + this.id);
		}
		int jf = 0;
		for(OrderItem item : this.orderItems){
			jf += item.getJifen() * item.getNum();
		}
		return jf;
	}
	public ConsigneeAddress getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(ConsigneeAddress consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
