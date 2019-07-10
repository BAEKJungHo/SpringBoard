package com.mayeye.board.dao;

import java.util.List;
import java.util.Map;

import com.mayeye.board.dto.FileDetail;
import com.mayeye.board.dto.FileMaster;

public interface FilesDAO {
	
	public List<Map<String, Object>> selectFileDetailList(Map<String, Object> map);
	public void insertFileMaster(FileMaster fileMaster);
	public void insertFileDetail(FileDetail fileDetail);
	public FileDetail findFileDetail(FileDetail file);
	
}
