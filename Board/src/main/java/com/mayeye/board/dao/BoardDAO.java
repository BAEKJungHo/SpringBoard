package com.mayeye.board.dao;

import java.util.List;
import java.util.Map;

import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.dto.Criteria;
import com.mayeye.board.dto.SearchCriteria;

public interface BoardDAO {
	
	public List<BoardDTO> list();
	public void delete(BoardDTO boardDTO);
	public int update(BoardDTO boardDTO);
	public void insert(BoardDTO boardDTO);
	public BoardDTO select(int num);
	public int updateReadCount(int num);
	public List<Map<String, Object>> pageList(Criteria cri);
	public int countBoardList();
	public List<BoardDTO> searchList(SearchCriteria cri);
	public int countArticle(SearchCriteria cri);
	public String boardGetKey(int num);
}
