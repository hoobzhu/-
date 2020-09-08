package com.hoob.rs.security.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hoob.rs.comm.model.BaseEntity;

import com.hoob.rs.comm.model.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author mayjors
 * 2017年8月29日
 */
@Entity
@Table(name="privilege")
//@JsonIgnoreProperties(value={"parent"})
public class Privilege extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private String menuCode;//菜单代号（用于权限控制）   
	private String menuTitle;//菜单国际化标识
	private int menuLevel;//菜单层级
	private int menuType;//菜单类型(0：非叶子菜单,1:叶子菜单,2:菜单功能项)

	@JsonManagedReference
	private Privilege parent;

	@JsonBackReference
	private List<Privilege> childs;	
	private String description;
	private String url;//菜单功能项映射的URI	
	private int enable=0;//当前权限是否启用，0：启用 ，1：不启用启用
	

	@Column(columnDefinition="varchar(255) comment '描述'")
	public String getDescription() {
		return description;
	}

	public void setDescription(final String descriptionToSet) {
		description = descriptionToSet;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name="parentId")
	public Privilege getParent() {
		return parent;
	}

	public void setParent(Privilege parent) {
		this.parent = parent;
	}

	@OneToMany(targetEntity=Privilege.class, cascade={CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name = "parentId",columnDefinition="comment '父节点ID'")
	public List<Privilege> getChilds() {
		return childs;
	}

	public void setChilds(List<Privilege> childs) {
		this.childs = childs;
	}

	
	@Column(unique=true,nullable=false,columnDefinition="varchar(50) comment '菜单代号'")
	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	@Column(columnDefinition="varchar(50) comment '菜单国际化标识'")
	public String getMenuTitle() {
		return menuTitle;
	}

	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle;
	}

	@Column(columnDefinition="int comment '菜单层级'")
	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	@Column(columnDefinition="int comment '菜单类型'")
	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	@Column(columnDefinition="varchar(150) comment '接口链接'")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(nullable = false,columnDefinition = "int(2) default 0 comment '是否启用'")
	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
		if (o == null || getClass() != o.getClass()) {
            return false;
        }
		Privilege privilege = (Privilege) o;
		return menuLevel == privilege.menuLevel &&
				menuType == privilege.menuType &&
				enable == privilege.enable &&
				Objects.equals(menuCode, privilege.menuCode) &&
				Objects.equals(menuTitle, privilege.menuTitle) &&
				Objects.equals(parent, privilege.parent) &&
				Objects.equals(childs, privilege.childs) &&
				Objects.equals(description, privilege.description) &&
				Objects.equals(url, privilege.url);
	}

	@Override
	public int hashCode() {

		return Objects.hash(menuCode, menuTitle, menuLevel, menuType, parent, childs, description, url, enable);
	}
}
