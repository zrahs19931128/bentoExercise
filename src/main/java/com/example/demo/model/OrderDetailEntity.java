package com.example.demo.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_detail")
public class OrderDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detail_id")
	private int detailId;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "order_id")
	private OrderListEntity orderListEntity;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "menu_id")
	private BentoEntity bentoEntity;

	@Column(name = "order_count")
	private int orderCount;

	@Column(name = "total_price")
	private int totalPrice;

	@Column(name = "order_time")
	private Date orderTime;

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public OrderListEntity getOrderListEntity() {
		return orderListEntity;
	}

	public void setOrderListEntity(OrderListEntity orderListEntity) {
		this.orderListEntity = orderListEntity;
	}

	public BentoEntity getBentoEntity() {
		return bentoEntity;
	}

	public void setBentoEntity(BentoEntity bentoEntity) {
		this.bentoEntity = bentoEntity;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
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
