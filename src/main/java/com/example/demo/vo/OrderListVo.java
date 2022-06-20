package com.example.demo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class OrderListVo {

	private int orderId;

	private String orderNumber;

	private String orderAccount;

	private String orderName;

	private int totalPrice;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;

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
}
