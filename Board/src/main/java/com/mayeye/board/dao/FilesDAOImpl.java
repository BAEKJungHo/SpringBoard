package com.mayeye.board.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mayeye.board.dto.FileDetail;
import com.mayeye.board.dto.FileMaster;

@Repository
public class FilesDAOImpl implements FilesDAO {
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<Map<String, Object>> selectFileDetailList(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("filesDAO.selectFileList", map);
	}

	@Override
	public void insertFileMaster(FileMaster fileMaster) {
		sqlSessionTemplate.insert("filesDAO.insertFileMaster", fileMaster);
		
	}

	@Override
	public void insertFileDetail(FileDetail fileDetail) {
		sqlSessionTemplate.insert("filesDAO.insertFileDetail", fileDetail);
	}

	@Override
	public FileDetail findFileDetail(FileDetail file) {
		return sqlSessionTemplate.selectOne("filesDAO.findFileDetail", file);
	}
	
}
