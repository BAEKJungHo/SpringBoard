package com.mayeye.board.service;

import java.util.List;
import java.util.Map;

import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.dto.Criteria;

public interface BoardService {
	
	public List<BoardDTO> list();
	public void delete(BoardDTO boardDTO);
	public int edit(BoardDTO boardDTO);
	public void write(BoardDTO boardDTO);
	public BoardDTO read(int num);
	List<Map<String, Object>> pageList(Criteria cri);
	public int countBoardList();
	
}
