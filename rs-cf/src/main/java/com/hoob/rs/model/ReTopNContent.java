package com.hoob.rs.model;



/**
 * @author Hoob 2020年8月7日
 */

public class ReTopNContent  {
	private static final long serialVersionUID = 1L;
	

    private String contentId;//内容Id
    private int playCount;//用户自增Id
	
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public int getPlayCount() {
		return playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}
   
}
