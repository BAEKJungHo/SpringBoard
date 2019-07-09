package com.mayeye.board.service;

import java.util.List;
import java.util.Map;

import com.mayeye.board.dto.FilesDTO;

public interface FilesService {
	
	public List<Map<String, Object>> selectFileList(Map<String, Object> map);
	public void insert(FilesDTO filesDTO);

}
