package com.example.demo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "member")
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "account_name")
	private String accountName;
	
	@Column(name = "member_name")
	private String memberName;
	
	@Column(name = "password")
	private String password;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "author_id")
	private AuthorityEntity authorityEntity;
	
	@Column(name = "add_time")
	private Date addTime;
	
	@Column(name = "update_time")
	private Date updateTime;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "memberEntity")
	private Set<MemberAuthorEntity> memberAuthorEntity = new HashSet<MemberAuthorEntity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AuthorityEntity getAuthorityEntity() {
		return authorityEntity;
	}

	public void setAuthorityEntity(AuthorityEntity authorityEntity) {
		this.authorityEntity = authorityEntity;
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

	public Set<MemberAuthorEntity> getMemberAuthorEntity() {
		return memberAuthorEntity;
	}

	public void setMemberAuthorEntity(Set<MemberAuthorEntity> memberAuthorEntity) {
		this.memberAuthorEntity = memberAuthorEntity;
	}
	
	public void addMemberAuthor(MemberAuthorEntity memberAuthorEntity) {
		if (!this.memberAuthorEntity.contains(memberAuthorEntity)) {
			this.memberAuthorEntity.add(memberAuthorEntity);
			memberAuthorEntity.setMemberEntity(this);;
		}
	}
}
