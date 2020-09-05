package com.hoob.rs.model;

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
@Table(name = "topn_recommendation",indexes={
		@Index(columnList="contentId",unique=true),
		@Index(columnList="programType",unique=false),
		@Index(columnList="playCount",unique=false)})
public class ReTopNContent extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
    private String contentId;//内容Id
    private int playCount;//用户自增Id
    private String programType;
    
    @Column(columnDefinition = "varchar(10) comment '物理分类'")
	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	@Column(columnDefinition="int comment '内容播放次数'")
	public int getPlayCount() {
		return playCount;
	}

	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	@Column(columnDefinition = "varchar(50) comment '内容唯一标识'")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}		

}
