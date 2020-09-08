/**
 * 
 */
package com.hoob.rs.comm.vo;


public class BaseMediaVO extends BaseVO {	
	
	
	//CMS定义的有意义的唯一标识
	private String contentId;	

	//媒体类型
	private int mediaType;	
		
	//内容提供商
	private String cpId;

	//内容提供商提供的内容编号
	private String cpCode;

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}	

	
	
}
