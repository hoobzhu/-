package com.hoob.rs.security.vo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;

import java.util.Date;

/**
 * @author mayjors
 * 2017年8月29日
 */
public class PrivilegeVO implements Comparable<PrivilegeVO>{

	private long id;
	private String menuCode;//菜单代号（用于权限控制）   
	private String menuTitle;//菜单国际化标识
	private int menuLevel;//菜单层级
	private int menuType;//菜单类型(0：非叶子菜单,1:叶子菜单,2:菜单功能项)

	@JsonIgnore
	@JsonSerialize
	@JsonBackReference
	private PrivilegeVO parent;
	private long parentId;
	//	private List<PrivilegeVO> childs;
	private String description;
	private String url;//菜单功能项映射的URI	
	private int enable=0;//当前权限是否启用，0：启用 ，1：不启用启用

	private Date createTime;
	private Date updateTime;
    /****/
	public PrivilegeVO() {

	}
    /****/
	public PrivilegeVO(long id, String menuCode, String menuTitle, int menuLevel,
			int menuType, String description, String url, int enable,
			Date createTime, Date updateTime,PrivilegeVO parent) {
		this.id = id;
		this.menuCode = menuCode;
		this.menuTitle = menuTitle;
		this.menuLevel = menuLevel;
		this.menuType = menuType;
		this.description = description;
		this.url = url;
		this.enable = enable;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.parent = parent;
	}
    /****/
	public PrivilegeVO(long id, String menuCode, String menuTitle, 
			int menuLevel, int menuType, String description, String url, int enable, long parentId) {
		this.id = id;
		this.menuCode = menuCode;
		this.menuTitle = menuTitle;
		this.menuLevel = menuLevel;
		this.menuType = menuType;
		this.description = description;
		this.url = url;
		this.enable = enable;
		this.parentId = parentId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String descriptionToSet) {
		description = descriptionToSet;
	}

	public PrivilegeVO getParent() {
		return parent;
	}

	public void setParent(PrivilegeVO parent) {
		this.parent = parent;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	//	public List<PrivilegeVO> getChilds() {
	//		return childs;
	//	}
	//
	//	public void setChilds(List<PrivilegeVO> childs) {
	//		this.childs = childs;
	//	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@JsonSerialize(using= CustomDateSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	@JsonDeserialize(using= CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonSerialize(using= CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}

	@JsonDeserialize(using= CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	@Override
	public int compareTo(PrivilegeVO o) {
		return (int) (this.getId()-o.getId());
	}
}
