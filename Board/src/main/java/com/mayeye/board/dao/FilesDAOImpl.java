package com.mayeye.board.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mayeye.board.dto.FilesDTO;

@Repository
public class FilesDAOImpl implements FilesDAO {
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<Map<String, Object>> selectFileList(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("filesDAO.selectFileList", map);
	}

	@Override
	public void insert(FilesDTO filesDTO) {
		sqlSessionTemplate.insert("filesDAO.insert", filesDTO);
	}
}
