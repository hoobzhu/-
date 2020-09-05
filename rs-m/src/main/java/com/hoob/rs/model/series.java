package com.hoob.rs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "series")
public class series extends BaseEntity {
	private static final long serialVersionUID = 1L;
	
	//主键
    private String contentId;//唯一id
    private String name;
    private String programType;//物理分类
    private String kind;//内容类型
    private String categorys;//栏目，只需叶子节点的
    private String cast;//演职人员包含导演演员，逗号隔开
    
    
    @Column(columnDefinition = "varchar(50) comment '合集contentid'")
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	@Column(columnDefinition = "varchar(50) comment '合集名称'")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition = "varchar(10) comment '物理分类'")
	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}
	@Column(columnDefinition = "varchar(50) comment '内容类型'")
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	@Column(columnDefinition = "varchar(1024) comment '叶子栏目唯一标识'")
	public String getCategorys() {
		return categorys;
	}

	public void setCategorys(String categorys) {
		this.categorys = categorys;
	}
	@Column(columnDefinition = "varchar(512) comment '演职人员'")
	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

}
