package com.mayeye.board.dao;

import com.mayeye.board.dto.UsersDTO;

public interface UsersDAO {
	
	public UsersDTO select(UsersDTO usersDTO);
	public boolean checkPw(String id, String pwd);

}
