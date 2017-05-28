package cn.devmgr.sample.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

public class Order implements Serializable{
	private static final long serialVersionUID = -2490657086698549998L;
	
	private int id;
	@Null
	private Date orderDate;
	
	//@Valid注解表示这个属性也需要验证
	@Valid
	@NotNull
	private ConsigneeAddress consigneeAddress;

	@Valid
	@NotNull
	@Size(min=1, message="至少需要有一件商品")
	private List<OrderItem> orderItems;
	
	@Null
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
