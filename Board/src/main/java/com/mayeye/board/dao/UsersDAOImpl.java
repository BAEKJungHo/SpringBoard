package com.mayeye.board.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mayeye.board.dto.UsersDTO;

@Repository
public class UsersDAOImpl implements UsersDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public UsersDTO select(String id) {
		UsersDTO dto = (UsersDTO)sqlSessionTemplate.selectOne("select", id);
		return dto;
	}

}
