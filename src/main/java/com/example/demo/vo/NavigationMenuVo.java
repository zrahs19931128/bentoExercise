package com.example.demo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigationMenuVo {

	private int id;
	
	@JsonProperty("text")
	private String name;
	
	private String url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
