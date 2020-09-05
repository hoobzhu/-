package com.hoob.rs.security.vo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;

/**
 * @author mayjors
 * 2017年9月4日
 */
public class RoleVO {
	
	private long id; 
	private String name;
	private String description;
	private String creator; //创建者

	private Date createTime;
	private Date updateTime;

	private List<Long> priId;

	@JsonSerialize
	private Set<PrivilegeVO> privileges;

	public RoleVO() {

	}

	public RoleVO(long id, String name, String description, String creator, Date createTime, Date updateTime) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.creator = creator;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(final String nameToSet) {
		name = nameToSet;
	}

	public List<Long> getPriId() {
		return priId;
	}

	public void setPriId(List<Long> priId) {
		this.priId = priId;
	}

	public Set<PrivilegeVO> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<PrivilegeVO> privileges) {
		this.privileges = privileges;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	@JsonDeserialize(using= CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}

	@JsonDeserialize(using= CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
