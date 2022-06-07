package com.example.demo.util;

public enum AuthorEnum {

	ADMIN(1,"Admin"),
	NORMAL(2,"Normal");
	
	private int code;
	private String author;
	
	public String toString() {
		return this.name()+"("+this.code+","+this.author+")";
	}
	
	public static String getAuthor(int code) {
		for(AuthorEnum authorEnum : AuthorEnum.values()) {
			if(authorEnum.getCode() == code) {
				return authorEnum.author;
			}
		}
		return null;
	}
	
	private AuthorEnum(int code, String author) {
		this.code = code;
		this.author = author;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
