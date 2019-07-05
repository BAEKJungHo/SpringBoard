package com.mayeye.board.service;

import com.mayeye.board.dto.UsersDTO;

public interface UsersService {
	
	public UsersDTO select(UsersDTO usersDTO);
	public boolean checkPw(String id, String pwd);

}
