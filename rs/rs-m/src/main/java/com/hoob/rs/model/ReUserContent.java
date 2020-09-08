package com.hoob.rs.model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hoob.rs.comm.model.BaseEntity;

/**
 * @author Hoob 2020年8月7日
 */
@Entity
@Table(name = "user_recommendation",indexes={
		@Index(columnList="userId",unique=true)})
public class ReUserContent extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	//主键
    private String userId;
    private String contentIds;//内容Id

    @Column(columnDefinition = "varchar(1024) comment '用户推荐内容id'")
	public String getContentIds() {
		return contentIds;
	}

	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}

	@Column(columnDefinition = "varchar(50) comment '用户唯一标识'")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}//唯一id
    
	
    
    
    
	

}
