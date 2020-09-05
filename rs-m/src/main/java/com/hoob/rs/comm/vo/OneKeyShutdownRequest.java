package com.hoob.rs.comm.vo;

import java.util.List;

public class OneKeyShutdownRequest extends Request {

	private List<String> contentIds;
	private int type;//0:关停 1：恢复
	private String movieContentId;

	public List<String> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<String> contentIds) {
		this.contentIds = contentIds;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMovieContentId() {
		return movieContentId;
	}

	public void setMovieContentId(String movieContentId) {
		this.movieContentId = movieContentId;
	}
	
}
