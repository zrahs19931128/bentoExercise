package com.example.demo.model;

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
@Table(name = "member_author")
public class MemberAuthorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "member_id")
	private MemberEntity memberEntity;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "author_id")
	private AuthorityEntity authorityEntity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MemberEntity getMemberEntity() {
		return memberEntity;
	}

	public void setMemberEntity(MemberEntity memberEntity) {
		this.memberEntity = memberEntity;
	}

	public AuthorityEntity getAuthorityEntity() {
		return authorityEntity;
	}

	public void setAuthorityEntity(AuthorityEntity authorityEntity) {
		this.authorityEntity = authorityEntity;
	}
}
