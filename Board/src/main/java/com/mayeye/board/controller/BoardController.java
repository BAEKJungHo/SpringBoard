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
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	/** @Resource
	* 객체의 이름이 일치하는 객체 자동주입 
	* name 속성명에는 IOC 컨테이너에서 설정한 id 명으로 입력 
	* 스프링 설정 파일을 불러올 때 유용하게 쓰임
	* name 속성으로 지정한 uploadPath를 사용 할 수 있음
	*/
	@Resource(name="uploadPath")
	private String uploadPath;
	
	/** 게시판 페이징 + 검색 */
	@RequestMapping(value="/boardSearchList")
	public String boardSearchList(SearchCriteria cri, Model model) {
		int count = boardService.countArticle(cri);
		
		// 페이지 생성
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(count);
		
		// 검색 조건과 페이지에 대한 정보를 리스트로 가져온다.
		List<BoardDTO> searchList = boardService.searchList(cri);
		
		model.addAttribute("searchList", searchList);
		model.addAttribute("searchType", cri.getSearchType());
		model.addAttribute("count", count);
		model.addAttribute("keyword", cri.getKeyword());
		model.addAttribute("pageMaker", pageMaker);
		
		return "boardSearchList";
	}
	
	/** 게시글 상세 내역 : 다중 업로드 적용 */
	@RequestMapping(value="/boardRead/{num}")
	public String boardRead(@ModelAttribute SearchCriteria cri, Model model, @PathVariable int num) {
		BoardDTO boardDTO = boardService.read(num);
		List<FileDetail> fileDetailList = filesService.findFileDetailList(boardDTO.getAtch_file_id());
		model.addAttribute("boardDTO", boardDTO);
		model.addAttribute("fileDetailList", fileDetailList);
		return "boardRead";
	}
	
	/** form 커스텀 태그 사용 : 바인딩을 위해 boardDTO 객체를 Model로 담아서 넘겨줌 */
	@RequestMapping(value="/boardWrite", method=RequestMethod.GET) 
	public String boardWrite(Model model) {
		model.addAttribute("boardDTO", new BoardDTO()); 
		return "boardWrite";
	}
	
	/** 게시글 작성 : 다중 업로드 버전
	* Hibernate-validator까지 처리한 코드
	* MultipartHttpServletRequest mtfRequest를 사용
	*/
	@RequestMapping(value="/boardWrite", method=RequestMethod.POST)
	public String boardWrite(@Valid BoardDTO boardDTO, BindingResult bindingResult, HttpSession session, MultipartHttpServletRequest mtfRequest, Model model) throws Exception {
		if(bindingResult.hasErrors()) {
			return "boardWrite"; // ViewResolver로 보냄
		} else {
			String atch_file_id = checkAtchFileId();
			int file_sn = 0;
			
			// 게시판
			boardDTO.setId((String)session.getAttribute("id"));
			boardDTO.setAtch_file_id(atch_file_id);
			
			// uploadFile() : 내가 보낸 파일 명이 아닌, 저장된 시스템 파일명 
			FileMaster fileMaster = new FileMaster();
			FileDetail fileDetail = new FileDetail();
			
			// 마스터 키값
			fileMaster.setAtch_file_id(atch_file_id);
			filesService.insertFileMaster(fileMaster);
			
			// getFiles안의 키값은 input multiple을 적용한 name값을 적으면 됩니다.
			// 파일을 등록하지 않더라도 name값을 따와서 그런지, 파일이 1개 있다고 가정되는듯
			// View의 폼에 파일 태그가 있으면, 파일객체가 생성되기때문에, 파일을 넣지않아도 1개로 size가 지정됨
			List<MultipartFile> fileList = mtfRequest.getFiles("file");
			
			// 파일이 빈 값인 경우 0으로 넘어온다.
			for (MultipartFile file: fileList) {
				if (file.getSize() > 0) {
					String save_name = uploadFile(file);
					model.addAttribute("save_name", save_name);
					
					// 파일 세부 사항
					fileDetail.setAtch_file_id(atch_file_id);
					fileDetail.setFile_sn(file_sn);
					fileDetail.setOri_name(file.getOriginalFilename());
					fileDetail.setSave_name(save_name);
					fileDetail.setSave_path(uploadPath);
					fileDetail.setFile_size((int)file.getSize());
					
					filesService.insertFileDetail(fileDetail);
					Logger.info("file_sn ..... " + String.valueOf(file_sn));
					file_sn++;
				}
			}
			
			boardService.write(boardDTO);
			return "redirect:/boardSearchList"; 
		}
	}
	
	/** 서버에 저장할 파일명 생성
		* UUID는 유니크한 ID값을 random으로 toString()화 하고 뒤에 오리지널 파일 명과 합침
		* 장점 : 성능도 괜찮고, 유니크한 ID값을 가질 수있다. 
		* 단점 : 파일 명이 길어진다.
		* 서버에 저장된 파일명을 가져와서 uploadPath 경로에 만들고 FileCopyUtils.copy()를 이용해서 쓴다.
		* MultipartFile의 getBytes() 메서드를 이용하여 target에 데이터를 쓴다.
	 */
	private String uploadFile(MultipartFile file) throws Exception {
		String save_name = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		File target = new File(uploadPath, save_name);
		FileCopyUtils.copy(file.getBytes(), target);
		return save_name;
	}
	
	/** atch_file_id 생성 : char(25) */
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
	
	/** atch_file_id 생성 검증(중복 제거) */
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
	
	/** 게시글 수정권한 판단 */
	@RequestMapping(value="/boardEdit/{num}", method=RequestMethod.GET)
	public String boardEdit(@PathVariable int num, Model model, HttpSession session, RedirectAttributes rttr) {
		if(session.getAttribute("id").equals(boardService.read(num).getId())) {
			BoardDTO boardDTO = boardService.read(num);
			List<FileDetail> fileDetailList = filesService.findFileDetailList(boardDTO.getAtch_file_id());
			
			model.addAttribute("oldKey", boardDTO.getAtch_file_id());
			model.addAttribute("boardDTO", boardDTO);
			model.addAttribute("fileDetailList", fileDetailList);
			return "boardEdit";	
		} else {
			rttr.addFlashAttribute("msg", "수정 권한이 없습니다");
			return "redirect:/boardSearchList";
		}
	}
	
	/** 수정 검증
	* BindingResut + Validator
	* 파일 추가 
	* 게시글에 키값이 '0'인경우 새로운 키 값 생성
	* 게시글에 키값이 존재하는 경우, 그 키로 물려준다.
	*/
	@RequestMapping(value="/boardEdit/{num}", method=RequestMethod.POST)
	public String boardEdit(@Valid BoardDTO boardDTO, @PathVariable int num, BindingResult bindingResult, MultipartHttpServletRequest mtfRequest, Model model, HttpSession session) throws Exception {
		if(bindingResult.hasErrors()) return "boardEdit";
		else {
			FileDetail fileDetail = new FileDetail();
			
			// 기존 파일이 없는 경우 file_sn, atch_file_key 설정
			int file_sn = 0;
			String atch_file_id = boardService.boardGetKey(num);
			if(filesService.checkDataToFD(num) != 0)  {
				file_sn = filesService.getKeyByNum(num).getFile_sn();	
			}
			
			// getFiles안의 키값은 input multiple을 적용한 name값을 적으면 됩니다.
			// MultipartHttpServletRequest는 방금 업로드한 파일을 가져오는거지, 이미 업로드된 파일을 가져오진 않는다.
			List<MultipartFile> fileList = mtfRequest.getFiles("file");
			
			for (MultipartFile file: fileList) {
				if (file.getSize() > 0) {
					// 파일 세부 사항
					file_sn++;
					fileDetail.setAtch_file_id(atch_file_id);
					fileDetail.setFile_sn(file_sn);
					fileDetail.setOri_name(file.getOriginalFilename());
					fileDetail.setSave_name(uploadFile(file));
					fileDetail.setSave_path(uploadPath);
					fileDetail.setFile_size((int)file.getSize());
					
					filesService.insertFileDetail(fileDetail);
				} else boardDTO.setAtch_file_id(fileDetail.getAtch_file_id());
			}
			
			// 제목 내용 변경
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
