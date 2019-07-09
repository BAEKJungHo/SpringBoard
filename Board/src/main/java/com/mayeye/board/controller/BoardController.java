package com.mayeye.board.controller;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.dto.Criteria;
import com.mayeye.board.dto.PageMaker;
import com.mayeye.board.dto.SearchCriteria;
import com.mayeye.board.service.BoardService;
import com.mayeye.board.service.FilesService;

@Controller
public class BoardController {
	
	private static final Logger Logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private BoardService boardService;
	
	@Autowired
	private FilesService filesService;
	
	/* 객체의 이름이 일치하는 객체 자동주입 
	* name 속성명에는 IOC 컨테이너에서 설정한 id 명으로 입력 
	* 스프링 설정 파일을 불러올 때 유용하게 쓰임
	* name 속성으로 지정한 uploadPath를 사용 할 수 있음
	*/
	@Resource(name="uploadPath")
	private String uploadPath;
	
	// 게시판 페이징 + 검색
	@RequestMapping(value="/boardSearchList")
	public ModelAndView list(SearchCriteria cri) {
		Logger.info("!!!!!search!!!!!");
		
		ModelAndView mav = new ModelAndView();
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(boardService.countArticle(cri.getSearchType(), cri.getKeyword()));
		
		List<BoardDTO> searchList = boardService.searchList(cri);
		int count = boardService.countArticle(cri.getSearchType(), cri.getKeyword());
		Logger.info("searchType     " + cri.getSearchType());
		Logger.info("keyword     " + cri.getKeyword());
		Logger.info("svf     " + String.valueOf(count));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("searchList", searchList);
		map.put("count", count);
		map.put("searchOption", cri.getSearchType());
		map.put("keyword", cri.getKeyword());
		mav.addObject("pageMaker", pageMaker);
		mav.addObject("map", map);
		return mav;
	}
	
	// 게시판 페이징
	@RequestMapping(value="/boardPageList")
	public ModelAndView boardPageList(Criteria cri, HttpSession session) {
		ModelAndView mav = null;
		if(session.getAttribute("id") == null) {
			mav = new ModelAndView("redirect");
			mav.addObject("msg", "잘못된 접근 입니다!!");
			mav.addObject("url", "login");
			return mav;
		} else {
			mav = new ModelAndView("/boardPageList");
			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(boardService.countBoardList());
			
			List<Map<String, Object>> list = boardService.pageList(cri);
		
			mav.addObject("list", list);
			mav.addObject("pageMaker", pageMaker);
			
			return mav;
		}
	}

	// 게시판 목록
	@RequestMapping(value="/boardList")
	public String boardList(Model model, HttpSession session) {
		if(session.getAttribute("id") == null) {
			model.addAttribute("msg", "잘못된 접근입니다!!");
			model.addAttribute("url", "login");
			return "redirect";
		} else {
			model.addAttribute("boardList", boardService.list());
			model.addAttribute("user", session.getAttribute("user"));
			return "boardList";
		}
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
	
	// 게시글 작성
	// Hibernate-validator까지 처리한 코드
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@Valid BoardDTO boardDTO, BindingResult bindingResult, HttpSession session, MultipartFile file, Model model) throws Exception {
		Logger.info("upload POST .....OriginalName={}, size={}", file.getOriginalFilename(), file.getSize());
		if(bindingResult.hasErrors()) {
			return "boardWrite"; // ViewResolver로 보냄
		} else {
			/* uploadFile() : 내가 보낸 파일 명이 아닌, 저장된 시스템 파일명 */  
			String savedFileName = uploadFile(file); 
			model.addAttribute("savedFileName", savedFileName);
			
			boardDTO.setFile_key(savedFileName);
			boardDTO.setId((String)session.getAttribute("id"));
			
			boardService.write(boardDTO);
			return "redirect:/boardSearchList"; // 새글을 반영하기 위해 컨트롤러로 보냄
		}
	}
	
	// 업로드에 관한 파일 로직
	private String uploadFile(MultipartFile file) throws Exception {
		/* UUID는 유니크한 ID값을 random으로 toString()화 하고 뒤에 오리지널 파일 명과 합침
		* 장점 : 성능도 괜찮고, 유니크한 ID값을 가질 수있다. 
		* 단점 : 파일 명이 길어진다.
		* 서버에 저장된 파일명을 가져와서 uploadPath 경로에 만들고 FileCopyUtils.copy()를 이용해서 쓴다.
		*/
		String savedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		File target = new File(uploadPath, savedFileName);
		FileCopyUtils.copy(file.getBytes(), target);
		return savedFileName;
	}
	
	// 게시글 수정권한 판단
	@RequestMapping(value="/boardEdit/{num}", method=RequestMethod.GET)
	public String boardEdit(@PathVariable int num, Model model, HttpSession session, RedirectAttributes rttr) {
		if(session.getAttribute("id").equals(boardService.read(num).getId())) {
			 model.addAttribute("boardDTO", boardService.read(num));
			return "boardEdit";	
		} else {
			rttr.addFlashAttribute("msg", "수정 권한이 없습니다");
			return "redirect:/boardSearchList";
		}
	}
	
	// 수정 검증 : BindingResut + Validator
	@RequestMapping(value="/boardEdit/{num}", method=RequestMethod.POST)
	public String boardEdit(@Valid BoardDTO boardDTO, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) return "boardEdit";
		else {
			boardService.edit(boardDTO);
			return "redirect:/boardSearchList";
		}
	}
	
	// 게시글 삭제
	@RequestMapping(value="/boardDelete/{num}", method=RequestMethod.GET)
	public String boardDelete(@PathVariable int num, Model model, HttpSession session, RedirectAttributes rttr) {
		if(session.getAttribute("id").equals(boardService.read(num).getId())) {
			boardService.delete(boardService.read(num));
			return "redirect:/boardSearchList";
		} else {
			rttr.addFlashAttribute("msg", "삭제 권한이 없습니다.");
			return "redirect:/boardSearchList";
		}
	}
}
