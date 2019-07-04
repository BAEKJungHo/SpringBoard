package com.mayeye.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayeye.board.dao.BoardDAO;
import com.mayeye.board.dto.BoardDTO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDAO boardDAO;
	
	// BoardDAO를 가져오는 getter 메소드 
	public BoardDAO getBoardDAO() {
		return boardDAO;
	}
	
	// BoardDAO에 값을 넣는 setter 메소드 
	public void setBoardDAO(BoardDAO boardDAO) {
		this.boardDAO = boardDAO;
	}
	
	@Override
	public List<BoardDTO> list() {
		return boardDAO.list();
	}

	@Override
	public int delete(BoardDTO boardDTO) {
		return boardDAO.delete(boardDTO);
	}

	@Override
	public int edit(BoardDTO boardDTO) {
		return boardDAO.update(boardDTO);
	}

	@Override
	public void write(BoardDTO boardDTO) {
		boardDAO.insert(boardDTO);
	}
	
	@Override
	public BoardDTO read(int num) {
		boardDAO.updateReadCount(num);
		return boardDAO.select(num);
	}

}
