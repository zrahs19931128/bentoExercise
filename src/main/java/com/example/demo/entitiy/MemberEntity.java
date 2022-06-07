package com.example.demo.entitiy;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MemberEntity {

	private int id;
	
	private String account_name;
	
	private String member_name;
	
	private String password;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date add_time;
	
//	private List<MemberAuthority> authorities;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getAccount_name() {
		return account_name;
	}
	
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	
	public String getMember_name() {
		return member_name;
	}
	
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	
	public Date getAdd_time() {
		return add_time;
	}
	
	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
//	public List<MemberAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(List<MemberAuthority> authorities) {
//        this.authorities = authorities;
//    }
}
