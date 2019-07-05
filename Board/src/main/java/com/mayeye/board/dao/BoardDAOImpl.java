package com.mayeye.board.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mayeye.board.dto.BoardDTO;

@Repository
public class BoardDAOImpl implements BoardDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	
	// 스프링 4.3 이상이면 
	// 빈으로 등록되는 클래스에 생성자가 하나만있고
	// 해당 의존성으로 받는 클래스가 빈으로 등록되있으면 생략가
	/*
	 * @Autowired public BoardDAOImpl (SqlSessionTemplate sqlSessionTemplate) {
	 * this.sqlSessionTemplate = sqlSessionTemplate; }
	 */
	
	public void setSqlMapClient(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
	@Override
	public List<BoardDTO> list() {
		return sqlSessionTemplate.selectList("boardDAO.list");
	}
	@Override
	public int delete(BoardDTO boardDTO) {
		return sqlSessionTemplate.delete("boardDAO.delete", boardDTO);
	}
	@Override
	public int update(BoardDTO boardDTO) {
		return sqlSessionTemplate.update("boardDAO.update", boardDTO);
	}
	@Override
	public void insert(BoardDTO boardDTO) {
		sqlSessionTemplate.insert("boardDAO.insert", boardDTO);
	}
	@Override
	public BoardDTO select(int num) {
		BoardDTO dto = (BoardDTO)sqlSessionTemplate.selectOne("boardDAO.select", num);
		return dto;
	}
	@Override
	public int updateReadCount(int num) {
		return sqlSessionTemplate.update("boardDAO.updateCount", num);
	}
}
