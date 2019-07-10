package com.mayeye.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayeye.board.dao.FilesDAO;
import com.mayeye.board.dto.FileDetail;
import com.mayeye.board.dto.FileMaster;

@Service
public class FilesServiceImpl implements FilesService {

	@Autowired
	FilesDAO filesDAO;
	
	public List<Map<String, Object>> selectFileDetailList(Map<String, Object> map) {
		return filesDAO.selectFileDetailList(map);
	}
	
	@Override
	public void insertFileMaster(FileMaster fileMaster) {
		filesDAO.insertFileMaster(fileMaster);
		
	}

	@Override
	public void insertFileDetail(FileDetail fileDetail) {
		filesDAO.insertFileDetail(fileDetail);
	}

	@Override
	public List<FileDetail> findFileDetailList(String atch_file_no) {
		return filesDAO.findFileDetailList(atch_file_no);
	}
	
}
