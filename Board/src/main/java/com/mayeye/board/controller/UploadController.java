package com.mayeye.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {
	
	private static final Logger Logger = LoggerFactory.getLogger(BoardController.class);
	
	/* 객체의 이름이 일치하는 객체 자동주입 
	* name 속성명에는 IOC 컨테이너에서 설정한 id 명으로 입력 
	* 스프링 설정 파일을 불러올 때 유용하게 쓰임
	* name 속성으로 지정한 uploadPath를 사용 할 수 있음
	*/
	@Resource(name="uploadPath")
	private String uploadPath;
	
	@RequestMapping(value="/uploadForm", method=RequestMethod.GET)
	public void uploadFormGET() {
		Logger.info("upload GET ..... ");
	}
	
	/* Dependency에 commons 의존성 추가
	 * IOC 컨테이너에 multipartResolver 추가 했으므로 MulipartFile 사용가능
	 */
	@RequestMapping(value="/uploadForm", method=RequestMethod.POST)
	public void uploadFormPOST(MultipartFile file, Model model, @RequestParam String type) throws Exception {
		Logger.info("upload POST .....OriginalName={}, size={}", file.getOriginalFilename(), file.getSize());
		
		/* uploadFile() : 내가 보낸 파일 명이 아닌, 저장된 시스템 파일명 */  
		String savedFileName = uploadFile(file); 
		model.addAttribute("savedFileName", savedFileName);
		model.addAttribute("type", type);
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

}
