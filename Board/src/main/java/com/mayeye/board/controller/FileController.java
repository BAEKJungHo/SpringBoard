package com.mayeye.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mayeye.board.dto.FileDetail;
import com.mayeye.board.service.FilesService;

@Controller
public class FileController {
	
	private static final Logger Logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private FilesService filesService;
	
	/* 객체의 이름이 일치하는 객체 자동주입 
	* name 속성명에는 IOC 컨테이너에서 설정한 id 명으로 입력 
	* 스프링 설정 파일을 불러올 때 유용하게 쓰임
	* name 속성으로 지정한 uploadPath를 사용 할 수 있음
	*/
	
	@RequestMapping(value="/fileDownload/{num}/{atch_file_id}/{file_sn}")
	public String fileDownload(@PathVariable int num, @PathVariable String atch_file_id, @PathVariable int file_sn, 
			HttpServletRequest request,HttpServletResponse response) {
		
		FileDetail fileDetail = new FileDetail();
		fileDetail.setAtch_file_id(atch_file_id);
		fileDetail.setFile_sn(file_sn);
		fileDetail = filesService.findFileDetail(fileDetail);
		
		String path = fileDetail.getSave_path(); // 저장된 경로
		String fileName = fileDetail.getOri_name(); // 원본 파일명
		String savedName = fileDetail.getSave_name(); // 저장된 파일명
		String downName = null;
		String browser = request.getHeader("User-Agent"); // 브라우저 종류를 가져오는것
		
		File file = new File(path, savedName);
		
	    FileInputStream fileInputStream = null;
	    ServletOutputStream servletOutputStream = null;
	    
		try {
		// 파일 인코딩 
	        if(browser.contains("MSIE") || browser.contains("Trident") || browser.contains("Chrome")){ //브라우저 확인 파일명 encode  
	            downName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+", "%20");
	        } else{
				downName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
	        }
	        
	        response.setHeader("Content-Disposition","attachment;filename=\"" + downName +"\"");             
	        response.setContentType("application/octer-stream");
	        response.setHeader("Content-Transfer-Encoding", "binary;");
	        
	        fileInputStream = new FileInputStream(file);
	        // servletInputStream : 서버의 파일을 자바로 읽는다.
	        servletOutputStream = response.getOutputStream(); // 서버의 파일을 바깥쪽으로 보낼때
	 
	        byte b [] = new byte[1024];
	        int data = 0;
	 
	        while((data=(fileInputStream.read(b, 0, b.length))) != -1) {
	            servletOutputStream.write(b, 0, data);
	        }
	        
	        servletOutputStream.flush(); //출력
	        
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
	        if(servletOutputStream != null){
	            try{
	                servletOutputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	        if(fileInputStream != null){
	            try{
	                fileInputStream.close();
	            }catch (IOException e){
	                e.printStackTrace();
	            }
	        }
	    }
		return null;
	}
}
