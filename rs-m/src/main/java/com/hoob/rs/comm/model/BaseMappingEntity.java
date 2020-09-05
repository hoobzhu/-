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
public class BaseMappingEntity extends BaseEntity implements java.io.Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//媒体类型
	private int mediaType;		
	
	//可以用UUID
	private String contentId;	
	
	private int recycle = 0;// 回收站标记 0：非回收站 1：回收站
	
	@Column(nullable=false,columnDefinition="int(11) default 0 comment '回收站'")	
	public int getRecycle() {
		return recycle;
	}

	public void setRecycle(int recycle) {
		this.recycle = recycle;
	}
	
	@Column(nullable=false,unique=true,columnDefinition="varchar(32) comment '内容ID'")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	@Column(nullable=false,columnDefinition="int(11) comment '媒体类型'")	
	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	


	
}
