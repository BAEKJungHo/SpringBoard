package com.mayeye.board.service;

import java.util.List;

import com.mayeye.board.dto.BoardDTO;

public interface BoardService {
	
	public List<BoardDTO> list();
	public void delete(BoardDTO boardDTO);
	public int edit(BoardDTO boardDTO);
	public void write(BoardDTO boardDTO);
	public BoardDTO read(int num);

}
