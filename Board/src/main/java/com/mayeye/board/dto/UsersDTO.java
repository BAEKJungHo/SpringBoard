package com.mayeye.board.dto;

public class UsersDTO {
	
	private String id;
	private String name;
	private String pwd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Override
	public String toString() {
		return "UsersDTO [id=" + id + ", name=" + name + ", pwd=" + pwd + "]";
	}
}
