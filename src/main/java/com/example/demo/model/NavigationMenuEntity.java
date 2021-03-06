package com.example.demo.model;

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
@Table(name = "navigation_menu")
public class NavigationMenuEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "parentid")
	private int parentId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "sortorder")
	private int sortOrder;
	
	@Column(name = "isparent")
	private int isParent;
	
	@Column(name = "url")
	private String url;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "navigationMenuEntity")
	private Set<AuthorMenuEntity> authorMenuEntity = new HashSet<AuthorMenuEntity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getIsParent() {
		return isParent;
	}

	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<AuthorMenuEntity> getAuthorMenuEntity() {
		return authorMenuEntity;
	}

	public void setAuthorMenuEntity(Set<AuthorMenuEntity> authorMenuEntity) {
		this.authorMenuEntity = authorMenuEntity;
	}

	
	
}
