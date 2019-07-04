package com.mayeye.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayeye.board.dao.UsersDAO;
import com.mayeye.board.dto.UsersDTO;

@Service
public class UsersServiceImpl implements UsersService{
	
	@Autowired
	private UsersDAO usersDAO;
	
	public UsersDAO getUsersDAO() {
		return usersDAO;
	}
	
	public void setUsersDAO(UsersDAO usersDAO) {
		this.usersDAO = usersDAO;
	}

	@Override
	public UsersDTO select(String id) {
		return usersDAO.select(id);
	}

}
