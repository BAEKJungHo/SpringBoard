package com.mayeye.board.dto;

public class FilesDTO {

	private String file_key;
	private String ori_name;
	private String save_name;
	private String save_path;
	private String date;
	private String file_size;
	private String num;
	
	public String getFile_key() {
		return file_key;
	}
	
	public void setFile_key(String file_key) {
		this.file_key = file_key;
	}
	
	public String getOri_name() {
		return ori_name;
	}
	
	public void setOri_name(String ori_name) {
		this.ori_name = ori_name;
	}
	
	public String getSave_name() {
		return save_name;
	}
	
	public void setSave_name(String save_name) {
		this.save_name = save_name;
	}
	
	public String getSave_path() {
		return save_path;
	}
	
	public void setSave_path(String save_path) {
		this.save_path = save_path;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	@Override
	public String toString() {
		return "FilesDTO [file_key=" + file_key + ", ori_name=" + ori_name + ", save_name=" + save_name + ", save_path="
				+ save_path + ", date=" + date + ", file_size=" + file_size + ", num=" + num + "]";
	}
	
}
