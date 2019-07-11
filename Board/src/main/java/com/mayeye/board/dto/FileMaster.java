package com.mayeye.board.dto;

public class FileMaster {
	
	private String atch_file_id;
	private String create_dt;
	private String use_at;
	
	public String getAtch_file_id() {
		return atch_file_id;
	}
	
	public void setAtch_file_id(String atch_file_id) {
		this.atch_file_id = atch_file_id;
	}
	
	public String getCreate_dt() {
		return create_dt;
	}
	
	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}

	public String getUse_at() {
		return use_at;
	}

	public void setUse_at(String use_at) {
		this.use_at = use_at;
	}

	@Override
	public String toString() {
		return "FileMaster [atch_file_id=" + atch_file_id + ", create_dt=" + create_dt + ", use_at=" + use_at + "]";
	}

}
