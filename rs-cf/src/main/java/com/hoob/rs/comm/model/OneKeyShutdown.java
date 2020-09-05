/**
 * 
 */
package com.hoob.rs.comm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "shutdown", indexes = {
		@Index(columnList = "updateTime", unique = false),
		@Index(columnList = "createTime", unique = false),	
		@Index(columnList = "mediaType", unique = false),		
		@Index(columnList = "contentId", unique = false),
		@Index(columnList = "status", unique = false)
		})
@Entity
public class OneKeyShutdown extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private int mediaType = 2;//series
	
	private String contentId;
	
	private Integer status ; //null:初始   0：关停   1：恢复
	
	public static final int SHUTDOWN = 0;
	public static final int RENEW = 1;
	
	@Column(nullable=false,columnDefinition="varchar(32) comment '内容ID'")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	@Column(columnDefinition="int default 2 comment '媒体类型'",nullable=false)
	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	@Column(columnDefinition="int comment '关停状态'")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
}
