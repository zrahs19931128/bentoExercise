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
@Table(name = "authority")
public class AuthorityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "add_time")
	private Date addTime;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "authorityEntity")
	private Set<AuthorMenuEntity> authorMenuEntity = new HashSet<AuthorMenuEntity>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "authorityEntity")
	private Set<MemberEntity> memberEntity = new HashSet<MemberEntity>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "authorityEntity")
	private Set<MemberAuthorEntity> memberAuthorEntity = new HashSet<MemberAuthorEntity>();
	
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

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Set<AuthorMenuEntity> getAuthorMenuEntity() {
		return authorMenuEntity;
	}

	public void setAuthorMenuEntity(Set<AuthorMenuEntity> authorMenuEntity) {
		this.authorMenuEntity = authorMenuEntity;
	}

	public Set<MemberEntity> getMemberEntity() {
		return memberEntity;
	}

	public void setMemberEntity(Set<MemberEntity> memberEntity) {
		this.memberEntity = memberEntity;
	}

	public Set<MemberAuthorEntity> getMemberAuthorEntity() {
		return memberAuthorEntity;
	}

	public void setMemberAuthorEntity(Set<MemberAuthorEntity> memberAuthorEntity) {
		this.memberAuthorEntity = memberAuthorEntity;
	}
}
