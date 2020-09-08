package com.hoob.rs.sys.vo;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;




/**
 * 系统配置VO
 * 
 * @author Faker 2017年9月4日
 *
 */
public class SysConfigVO {

	private static final long serialVersionUID = 1L;

	private long id;

	private String key; // 配置KEY


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String value; // 配置值

    private String type;
	private Boolean enable = true; // 是否启用

	private String desc; // 描述

	private String name; // 名称
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}



	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}



	@Override
	public String toString() {
		return "SysConfig [key=" + key + ", value=" + value + ", enable=" + enable + ", desc=" + desc + ", name="
				+ name + "]";
	}

	@Column(name = "`name`", columnDefinition = "varchar(100) COMMENT '名称'")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
