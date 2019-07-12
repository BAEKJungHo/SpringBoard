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
	public List<FileDetail> findFileDetailList(String atch_file_no) {
		return sqlSessionTemplate.selectList("filesDAO.findFileDetailList", atch_file_no);
	}

	@Override
	public FileDetail findFileDetail(FileDetail fileDetail) {
		return sqlSessionTemplate.selectOne("filesDAO.findFileDetail", fileDetail);
	}

	@Override
	public void fileDelete(FileDetail fileDetail) {
		sqlSessionTemplate.update("filesDAO.fileDelete", fileDetail);
	}

	@Override
	public void masterDelete(String oldKey) {
		sqlSessionTemplate.delete("filesDAO.masterDelete", oldKey);
	}

	@Override
	public void updateBoardKey(FileDetail fileDetail) {
		sqlSessionTemplate.update("filesDAO.updateBoardKey", fileDetail);
	}

	@Override
	public void detailKeyUpdate(FileDetail fileDetail) {
		sqlSessionTemplate.update("filesDAO.detailKeyUpdate", fileDetail);
	}

	@Override
	public FileDetail getKeyByNum(int num) {
		return sqlSessionTemplate.selectOne("filesDAO.getKeyByNum", num);
	}

	@Override
	public int checkDataToFD(int num) {
		return sqlSessionTemplate.selectOne("filesDAO.checkDataToFD", num);
	}
}
