package com.mayeye.board.dao;

import java.util.List;

import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.dto.Criteria;

public interface BoardDAO {
	
	public List<BoardDTO> list();
	public void delete(BoardDTO boardDTO);
	public int update(BoardDTO boardDTO);
	public void insert(BoardDTO boardDTO);
	public BoardDTO select(int num);
	public int updateReadCount(int num);
	public List<BoardDTO> listCriteria(Criteria cri);
	public int totalPage(Criteria cri);

}
