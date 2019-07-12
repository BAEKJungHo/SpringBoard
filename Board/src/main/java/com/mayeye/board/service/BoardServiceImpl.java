package com.mayeye.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mayeye.board.dao.BoardDAO;
import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.dto.Criteria;
import com.mayeye.board.dto.SearchCriteria;

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
	public void delete(BoardDTO boardDTO) {
		boardDAO.delete(boardDTO);
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

	@Override
	public List<Map<String, Object>> pageList(Criteria cri) {
		return boardDAO.pageList(cri);
	}

	@Override
	public int countBoardList() {
		return boardDAO.countBoardList();
	}
	
	@Override
	public List<BoardDTO> searchList(SearchCriteria cri) {
		return boardDAO.searchList(cri);
	}
	
	@Override
	public int countArticle(String searchOption, String keyword) {
		return boardDAO.countArticle(searchOption, keyword);
	}

	@Override
	public String boardGetKey(int num) {
		return boardDAO.boardGetKey(num);
	}

}
