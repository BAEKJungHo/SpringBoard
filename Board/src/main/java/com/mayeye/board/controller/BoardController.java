package com.mayeye.board.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mayeye.board.dto.BoardDTO;
import com.mayeye.board.dto.Criteria;
import com.mayeye.board.dto.FileDetail;
import com.mayeye.board.dto.FileMaster;
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
	
	// 게시글 상세 내역 : 다중 업로드 적용
	@RequestMapping(value="/boardRead/{num}")
	public String boardRead(Model model, @PathVariable int num) {
		BoardDTO boardDTO = boardService.read(num);
		List<FileDetail> fileDetailList = filesService.findFileDetailList(boardDTO.getAtch_file_id());
		model.addAttribute("boardDTO", boardDTO);
		model.addAttribute("fileDetailList", fileDetailList);
		return "boardRead";
	}
	
	// a태그 혹은 주소창 입력시 들어오는 곳
	// 바인딩 에러처리를 위해 Model 객체와 model.addAttribute() 추가
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET) 
	public String boardWrite(Model model) {
		model.addAttribute("boardDTO", new BoardDTO()); 
		return "boardWrite";
	}
	
	/*
	// 게시글 작성 : 단일 업로드 버전 
	// Hibernate-validator까지 처리한 코드
	// MultipartFile을 이용하면 View에서 enctype="multipart/form-data" 처리한 폼의 파일 정보를 받을 수 있다.
	// MultipartFile의 각종 메서드 사용
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@Valid BoardDTO boardDTO, BindingResult bindingResult, HttpSession session, MultipartFile file, Model model) throws Exception {
		Logger.info("upload POST ..... OriginalName={}, size={}", file.getOriginalFilename(), file.getSize());
		if(bindingResult.hasErrors()) {
			return "boardWrite"; // ViewResolver로 보냄
		} else {
			// uploadFile() : 내가 보낸 파일 명이 아닌, 저장된 시스템 파일명 
			FileMaster fileMaster = new FileMaster();
			FileDetail fileDetail = new FileDetail();
			
			String atch_file_id = checkAtchFileId();

			String save_name = uploadFile(file); 
			model.addAttribute("save_name", save_name);
			
			Logger.info("SAVED FILENAME ....." + save_name);
			Logger.info("atch_file_id ....." + atch_file_id + " atch length ....." + atch_file_id.length());
			Logger.info("SAVED PATH ....." + uploadPath);
			
			// 파일 마스터 
			fileMaster.setAtch_file_id(atch_file_id);
			
			// 파일 세부 사항
			fileDetail.setAtch_file_id(atch_file_id);
			fileDetail.setFile_sn(file_sn);
			fileDetail.setOri_name(file.getOriginalFilename());
			fileDetail.setSave_name(save_name);
			fileDetail.setSave_path(uploadPath);
			fileDetail.setFile_size((int)file.getSize());
			
			// 게시판
			boardDTO.setAtch_file_id(atch_file_id);
			boardDTO.setId((String)session.getAttribute("id"));
			
			filesService.insertFileMaster(fileMaster);
			filesService.insertFileDetail(fileDetail);
			boardService.write(boardDTO);
			return "redirect:/boardSearchList"; // 새글을 반영하기 위해 컨트롤러로 보냄
		}
	}
	*/
	
	// 게시글 작성 : 다중 업로드 버전
	// Hibernate-validator까지 처리한 코드
	// MultipartHttpServletRequest mtfRequest를 사용
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@Valid BoardDTO boardDTO, BindingResult bindingResult, HttpSession session, MultipartHttpServletRequest mtfRequest, Model model) throws Exception {
		if(bindingResult.hasErrors()) {
			return "boardWrite"; // ViewResolver로 보냄
		} else {
			// file_sn 키 값 증가를 위한 변수 
			int file_sn = 0;
			
			// uploadFile() : 내가 보낸 파일 명이 아닌, 저장된 시스템 파일명 
			FileMaster fileMaster = new FileMaster();
			FileDetail fileDetail = new FileDetail();
			
			// getFiles안의 키값은 input multiple을 적용한 name값을 적으면 됩니다.
			List<MultipartFile> fileList = mtfRequest.getFiles("file");
			
			// 다중 파일이더라도 키값은 하나로 동일하고 file_sn만 따로 주면됨
			String atch_file_id = checkAtchFileId();
			
			// FileMaster 키값 설정
			fileMaster.setAtch_file_id(atch_file_id);
			filesService.insertFileMaster(fileMaster);
			
			// 게시판
			boardDTO.setAtch_file_id(atch_file_id);
			boardDTO.setId((String)session.getAttribute("id"));
			
			for (MultipartFile mf : fileList) {
				String save_name = uploadFile(mf);
				model.addAttribute("save_name", save_name);
				
				// 파일 세부 사항
				fileDetail.setAtch_file_id(atch_file_id);
				fileDetail.setFile_sn(file_sn);
				fileDetail.setOri_name(mf.getOriginalFilename());
				fileDetail.setSave_name(save_name);
				fileDetail.setSave_path(uploadPath);
				fileDetail.setFile_size((int)mf.getSize());
				
				filesService.insertFileDetail(fileDetail);
				Logger.info("file_sn ..... " + String.valueOf(file_sn));
				file_sn++;
			}				
			
			boardService.write(boardDTO);
			return "redirect:/boardSearchList"; 
		}
	}
	
	// 저장 파일명 생성
	private String uploadFile(MultipartFile file) throws Exception {
		/* UUID는 유니크한 ID값을 random으로 toString()화 하고 뒤에 오리지널 파일 명과 합침
		* 장점 : 성능도 괜찮고, 유니크한 ID값을 가질 수있다. 
		* 단점 : 파일 명이 길어진다.
		* 서버에 저장된 파일명을 가져와서 uploadPath 경로에 만들고 FileCopyUtils.copy()를 이용해서 쓴다.
		* MultipartFile의 getBytes() 메서드를 이용하여 target에 데이터를 쓴다.
		*/
		String save_name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		File target = new File(uploadPath, save_name);
		FileCopyUtils.copy(file.getBytes(), target);
		return save_name;
	}
	
	// atch_file_id 생성 : char(25)
	private String makeAtchFileId() {
		Random random = new Random();
		StringBuilder temp = new StringBuilder();
		for (int i=0; i<8; i++) {
		    int rIndex = random.nextInt(3);
		    switch (rIndex) {
		    case 0:
		        // a-z
		        temp.append((char) ((int) (random.nextInt(26)) + 97));
		        break;
		    case 1:
		        // A-Z
		        temp.append((char) ((int) (random.nextInt(26)) + 65));
		        break;
		    case 2:
		        // 0-9
		        temp.append((random.nextInt(10)));
		        break;
		    }
		}
		String atch_file_id = "FILE_" + "000000000000" + temp;
		return atch_file_id;
	}
	
	// atch_file_id 생성 검증(중복 제거)
	private String checkAtchFileId() {
		List<BoardDTO> list = new ArrayList<>();
		list = boardService.list();
		boolean flag = true;
		String atch_file_id = null;
		if(!list.isEmpty()) {
			while(flag) {
				atch_file_id = makeAtchFileId();
				for(BoardDTO board : list) {
					if(!atch_file_id.equals(board.getAtch_file_id())) {
						flag = false;
					} else flag = true;
				}
			}
			return atch_file_id;
		} else return atch_file_id = makeAtchFileId();
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
