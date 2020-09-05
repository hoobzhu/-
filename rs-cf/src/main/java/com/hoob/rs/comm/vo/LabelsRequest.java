package com.hoob.rs.comm.vo;

import java.util.List;

public class LabelsRequest extends Request {

	private List<String> contentIds;//内容id
	private List<String> codes;//角标code
	
	public List<String> getContentIds() {
		return contentIds;
	}
	public void setContentIds(List<String> contentIds) {
		this.contentIds = contentIds;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	

	
}
