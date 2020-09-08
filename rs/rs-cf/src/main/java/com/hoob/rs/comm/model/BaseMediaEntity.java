/**
 * 
 */
package com.hoob.rs.comm.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;



/**
 * @author Raul	
 * 2017年5月22日
 */

@MappedSuperclass
public class BaseMediaEntity extends BaseEntity implements java.io.Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	//CMS定义的有意义的唯一标识
	private String contentId;	

	//媒体类型
	private int mediaType;	
		
	//内容提供商
	private String cpId;

	//内容提供商提供的内容编号
	private String cpCode;	
	
	private int recycle = 0;// 回收站标记 0：非回收站 1：回收站
	
	@Column(nullable=false,columnDefinition="int(11) default 0 comment '回收站'")	
	public int getRecycle() {
		return recycle;
	}

	public void setRecycle(int recycle) {
		this.recycle = recycle;
	}
	

	@Column(nullable=false,columnDefinition="varchar(20) comment '内容提供商ID'")
	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	@Column(columnDefinition="varchar(50) comment '内容提供商提供的内容编号'")
	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	@Column(nullable=false,columnDefinition="int(11) comment '媒体类型'")	
	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	

	@Column(nullable=false,unique=true,columnDefinition="varchar(32) comment '内容ID'")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
}
