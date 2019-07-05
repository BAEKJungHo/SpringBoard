package com.mayeye.board.dao;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mayeye.board.dto.UsersDTO;

@Repository
public class UsersDAOImpl implements UsersDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public UsersDTO select(UsersDTO usersDTO) {
		UsersDTO dto = (UsersDTO)sqlSessionTemplate.selectOne("usersDAO.select", usersDTO.getId());
		return dto;
	}

	@Override
	public boolean checkPw(String id, String pwd) {
		boolean flag = false;
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("pwd", pwd);
		int count = sqlSessionTemplate.selectOne("usersDAO.checkPw", map);
		if(count == 1) return true;
		else return flag;
	}

}
