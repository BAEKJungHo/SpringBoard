package com.mayeye.board.dao;

import java.util.List;
import java.util.Map;

import com.mayeye.board.dto.FilesDTO;

public interface FilesDAO {
	
	public List<Map<String, Object>> selectFileList(Map<String, Object> map);
	public void insert(FilesDTO filesDTO);
	
}
