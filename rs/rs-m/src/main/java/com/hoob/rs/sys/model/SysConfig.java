package com.hoob.rs.sys.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.hoob.rs.comm.model.BaseEntity;


/**
 * 系统配置实体类
 * 
 * @author makefu 2016年4月14日
 *
 */
@Entity
@Table(name = "sys_config")
public class SysConfig extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;


	private String key; // 配置KEY


	private String value; // 配置值


	private Boolean enable = true; // 是否启用





	private String desc; // 描述

	private String name; // 名称

	private String type;// 类型，目前区分 config:系统参数 contant:常量

	@Column(name = "`key`", nullable = false,unique=true, 
			columnDefinition = "varchar(100) COMMENT '配置的KEY，不允许重复，用于区分不同的配置'")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "`value`", columnDefinition = "TEXT COMMENT '配置的具体内容，各配置格式自定'")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "`enable`", columnDefinition = "tinyint(1) COMMENT '是否启用'")
	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}



	@Column(name = "`desc`", columnDefinition = "varchar(1024) COMMENT '描述'")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	@Column(name = "`type`", columnDefinition = "varchar(20) default 'config' COMMENT '参数类型 参考值：config:系统配置，contant:系统常量'")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

}
