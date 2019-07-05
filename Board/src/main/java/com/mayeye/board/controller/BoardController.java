package com.mayeye.board.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.service.BoardService;

@Controller
@SessionAttributes("boardDTO")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	// 게시판 목록
	@RequestMapping(value="/boardList")
	public String boardList(Model model, HttpSession session) {
		model.addAttribute("boardList", boardService.list());
		model.addAttribute("user", session.getAttribute("user"));
		return "boardList";
	}
	
	// 게시글 상세 내역
	@RequestMapping(value="/boardRead/{num}")
	public String boardRead(Model model, @PathVariable int num) {
		model.addAttribute("boardDTO", boardService.read(num));
		return "boardRead";
	}
	
	// a태그 혹은 주소창 입력시 들어오는 곳
	// 바인딩 에러처리를 위해 Model 객체와 model.addAttribute() 추가
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET) 
	public String boardWrite(Model model) {
		model.addAttribute("boardDTO", new BoardDTO()); 
		return "boardWrite";
	}
	
	// form 태그로 POST 방식으로 지정
	
	/*
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(BoardDTO boardDTO) {
		boardService.write(boardDTO);
		return "redirect:/boardList";
	}
	* 이 방식은 너무 낙관적, 사용자가 제목, 내용등을 작성하지 않았거나, 
	* 숫자를 입력해야 하는 곳에 숫자로 변환 불가능한 값을 입력하면 바인딩 에러 처리를 위한 코드가 없음
	* 스프링 MVC는 이런 작업도 적은 코드로 작성할 수 있게 지원
	*/
	
	/* 바인딩 에러 처리한 코드
	* BindingResult객체의 hasErrors() 메서드 호출하면 바인딩할 때 오류가 있는 경우 true 반환
	*/ 
	/*
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(BoardDTO boardDTO, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "boardWrite"; // ViewResolver로 보냄
		} else {
			boardService.write(boardDTO);
			return "redirect:/boardList"; // 새글을 반영하기 위해 컨트롤러로 보냄
		}
	}
	*/
	
	// 게시글 작성
	// Hibernate-validator까지 처리한 코드
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@Valid BoardDTO boardDTO, BindingResult bindingResult, HttpSession session, Model model) {
		if(bindingResult.hasErrors()) {
			return "boardWrite"; // ViewResolver로 보냄
		} else {
			boardDTO.setId((String)session.getAttribute("id"));
			boardService.write(boardDTO);
			return "redirect:/boardList"; // 새글을 반영하기 위해 컨트롤러로 보냄
		}
	}
	
	// 게시글 수정권한 판단
	@RequestMapping(value="/boardEdit/{num}", method=RequestMethod.GET)
	public String boardEdit(@PathVariable int num, Model model, HttpSession session, RedirectAttributes rttr) {
		BoardDTO boardDTO = boardService.read(num);
		if(session.getAttribute("id").equals(boardDTO.getId())) {
			model.addAttribute("boardDTO", boardDTO);
			return "boardEdit";	
		} else {
			rttr.addFlashAttribute("msg", "수정 권한이 없습니다");
			return "redirect:/boardList";
		}
	}
	
	// 수정 검증
	@RequestMapping(value="/boardEdit/{num}", method=RequestMethod.POST)
	public String boardEdit(@Valid @ModelAttribute BoardDTO boardDTO, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) return "boardEdit";
		else {
			boardService.edit(boardDTO);
			return "redirect:/boardList";
		}
	}
	
	// 게시글 삭제 판단
	@RequestMapping(value="/boardDelete/{num}", method=RequestMethod.GET)
	public String boardDelete(@PathVariable int num, Model model, HttpSession session, RedirectAttributes rttr) {
		BoardDTO boardDTO = boardService.read(num);
		if(session.getAttribute("id").equals(boardDTO.getId())) {
			model.addAttribute("num", num);
			return "boardDelete";
		} else {
			rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
			return "redirect:/boardList";
		}
	}
	
	// 게시글 삭제
	@RequestMapping(value="/boardDelete", method=RequestMethod.POST)
	public String boardDelete(@PathVariable int num, Model model) {
		BoardDTO boardDTO = boardService.read(num);
		boardService.delete(boardDTO);
		return "redirect:/boardList";
	}
}
