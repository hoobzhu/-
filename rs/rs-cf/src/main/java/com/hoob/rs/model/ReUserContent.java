package com.hoob.rs.model;



/**
 * @author Hoob 2020年8月7日
 */

public class ReUserContent  {
	private static final long serialVersionUID = 1L;
	
	//	
    private String userId;
    private String contentIds;//内容Id
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContentIds() {
		return contentIds;
	}
	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}
}
