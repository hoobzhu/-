package com.hoob.rs.model;


/**
 * @author Hoob 2020年8月7日
 */

public class ReSimilarContent  {
	private static final long serialVersionUID = 1L;
	
	
    private String contentId;
    private String contentIds;//相似内容Id
	
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContentIds() {
		return contentIds;
	}
	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}
}
