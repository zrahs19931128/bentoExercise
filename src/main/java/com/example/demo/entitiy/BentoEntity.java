package com.example.demo.entitiy;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BentoEntity {

	private int menu_id;
	
	private String product_name;
	
	private int price_name;
	
	private int shelf_status;
	
	private int sell_day;
	
	private int sell_person;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date add_time;

	public int getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public int getPrice_name() {
		return price_name;
	}

	public void setPrice_name(int price_name) {
		this.price_name = price_name;
	}

	public int getShelf_status() {
		return shelf_status;
	}

	public void setShelf_status(int shelf_status) {
		this.shelf_status = shelf_status;
	}

	public int getSell_day() {
		return sell_day;
	}

	public void setSell_day(int sell_day) {
		this.sell_day = sell_day;
	}

	public int getSell_person() {
		return sell_person;
	}

	public void setSell_person(int sell_person) {
		this.sell_person = sell_person;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}
	
}
