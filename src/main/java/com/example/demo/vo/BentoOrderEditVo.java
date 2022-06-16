package com.example.demo.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BentoOrderEditVo {

	private int menuId;
	
	private String productName;
	
	private int price;
	
	private int shelfStatus;
	
	private int sellDay;
	
	private int sellPerson;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date addTime;
	
	private int orderCount;

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getShelfStatus() {
		return shelfStatus;
	}

	public void setShelfStatus(int shelfStatus) {
		this.shelfStatus = shelfStatus;
	}

	public int getSellDay() {
		return sellDay;
	}

	public void setSellDay(int sellDay) {
		this.sellDay = sellDay;
	}

	public int getSellPerson() {
		return sellPerson;
	}

	public void setSellPerson(int sellPerson) {
		this.sellPerson = sellPerson;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
}
