package com.hoob.rs.comm.vo;

import java.util.List;

public class PublishRequest extends Request {

	private List<String> contentIds;
	private int status;

	public List<String> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<String> contentIds) {
		this.contentIds = contentIds;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}
