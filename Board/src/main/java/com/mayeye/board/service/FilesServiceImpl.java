package com.mayeye.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayeye.board.dao.FilesDAO;
import com.mayeye.board.dto.FilesDTO;

@Service
public class FilesServiceImpl implements FilesService {

	@Autowired
	FilesDAO filesDAO;
	
	public List<Map<String, Object>> selectFileList(Map<String, Object> map) {
		return filesDAO.selectFileList(map);
	}

	@Override
	public void insert(FilesDTO filesDTO) {
		filesDAO.insert(filesDTO);
	}
	
}
