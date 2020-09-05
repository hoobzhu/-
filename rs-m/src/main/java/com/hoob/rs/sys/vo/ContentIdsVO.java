package com.hoob.rs.sys.vo;

import java.util.Date;

public class ContentIdsVO {

	private String contentId;//CMS定义的有意义的唯一标识
	private int mediaType;//媒体类型	
	private String cpId;//内容提供商
	private String cpCode;//内容提供商提供的内容编号	
	private int status;//使用状态
	private Date createTime;//创建时间
	private Date useTime;//最后更新时间	
	public String getContentId() {
		return contentId;
	}
	public int getMediaType() {
		return mediaType;
	}
	public String getCpId() {
		return cpId;
	}
	public String getCpCode() {
		return cpCode;
	}
	public int getStatus() {
		return status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Date getUseTime() {
		return useTime;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	public void setCpId(String cpId) {
		this.cpId = cpId;
	}
	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setUseTime(Date useTime) {
		this.useTime = useTime;
	}
}
