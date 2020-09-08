/**
 * 
 */
package com.hoob.rs.comm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;



/**
 * @author Raul	
 * 2017年5月22日
 */

@MappedSuperclass
public class BaseEntity implements java.io.Serializable{	
	



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//主键
	private long id;		
	
	//创建时间
	private Date createTime;

	//最后更新时间
	private Date updateTime;		
	

	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(nullable = false, columnDefinition = "DATETIME default CURRENT_TIMESTAMP comment '最后更新时间'")
	public Date getUpdateTime() {
		return updateTime;
	}
	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(nullable = false, columnDefinition = "DATETIME default CURRENT_TIMESTAMP comment '创建时间'")
	public Date getCreateTime() {
		return createTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}	
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
