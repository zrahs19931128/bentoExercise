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

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "bento_menu")
public class BentoEntity {

	@Id
	@Column(name = "menu_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int menuId;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "shelf_status")
	private int shelfStatus;
	
	@Column(name = "sell_day")
	private int sellDay;
	
	@Column(name = "sell_person")
	private int sellPerson;
	
	@Column(name = "add_time")
	private Date addTime;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bentoEntity")
	private Set<OrderDetailEntity> detailEntity = new HashSet<OrderDetailEntity>();

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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
