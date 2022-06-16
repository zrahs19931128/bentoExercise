package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "order_list")
public class OrderListEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int orderId;

	@Column(name = "order_number")
	private String orderNumber;

	@Column(name = "order_account")
	private String orderAccount;

	@Column(name = "order_name")
	private String orderName;

	@Column(name = "total_price")
	private int totalPrice;

	@Column(name = "order_time")
	private Date orderTime;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderListEntity")
	private Set<OrderDetailEntity> detailEntity = new HashSet<OrderDetailEntity>();

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderAccount() {
		return orderAccount;
	}

	public void setOrderAccount(String orderAccount) {
		this.orderAccount = orderAccount;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Set<OrderDetailEntity> getDetailEntity() {
		return detailEntity;
	}

	public void setDetailEntity(Set<OrderDetailEntity> detailEntity) {
		this.detailEntity = detailEntity;
	}

	public void addOrderDetail(OrderDetailEntity detailEntity) {
		if (!this.detailEntity.contains(detailEntity)) {
			this.detailEntity.add(detailEntity);
			detailEntity.setOrderListEntity(this);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("order_number :" + orderNumber + ",");
		sb.append("order_account :" + orderAccount + ",");
		sb.append("order_name :" + orderName + ",");
		sb.append("total_price :" + totalPrice + ",");
		sb.append("order_time :" + orderTime);
		return sb.toString();
	}
}
