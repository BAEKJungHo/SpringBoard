package com.mayeye.board.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class UsersDTO {
	
	@Length(min=5, max=20, message="ID는 5자 이상, 20자 이하 입력하세요.")
	private String id;
	@NotEmpty(message="이름을 입력하세요.")
	private String name;
	
	@Length(min=5, max=20, message="PWD는 5자 이상, 20자 이하 입력하세요.")
	@Pattern(regexp="^.*(?=.{6,20})(?=.*[0-9])(?=.*[a-zA-Z]).*$")
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
}
