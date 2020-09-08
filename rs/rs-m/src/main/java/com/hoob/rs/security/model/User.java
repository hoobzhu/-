package com.hoob.rs.security.model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.model.BaseEntity;

/**
 * @author mayjors 2017年8月29日
 */
@Entity
@Table(name = "user")
public class User extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public enum Type {
		/*
		 * 超级管理员，管理员，普通用户
		 * */
		SYSADMIN, ADMIN, USER
	}

	private String password;

	private String role;// 用来区分 syadmin sp cp

	private String nickName;// 昵称

	private String organization;// 所属机构

	private Date lastLoginTime;// 最近登录时间

	private Integer loginTimes = 0;// 登录次数

	private Boolean enable = true;// 是否启用

	private Boolean enableIPBinding = false; // 是否启用IP绑定

	private String ipAddr;

	private String userId; // 用户登录名

	private String parentUserId; // 父级ID

	private Integer userlevel = 1; // 层级 关系

	private String userType; // 超级管理员 管理员 用户 sysadmin admin user

	private Set<Role> roles;


	private Date pwdUpdateTime;// 最近密码更新时间

	@Column(columnDefinition = "varchar(1024) comment '用户密码'")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(columnDefinition = "varchar(10) comment '用户类型'")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(columnDefinition = "varchar(50) comment '用户昵称'")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(columnDefinition = "varchar(50) comment '用户所属机构'")
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Column(columnDefinition = "DATETIME comment '最近登录时间'")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(nullable = false, columnDefinition = "int(10) comment '登录次数'")
	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@Column(columnDefinition = "bit(1) comment '是否启用'")
	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Column(columnDefinition = "bit(1) comment '是否启用IP绑定' default false")
	public Boolean getEnableIPBinding() {
		return enableIPBinding;
	}

	public void setEnableIPBinding(Boolean enableIPBinding) {
		this.enableIPBinding = enableIPBinding;
	}

	@Column(columnDefinition = "text comment '绑定IP'")
	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	@Column(columnDefinition = "varchar(50) comment '用户名称'")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(columnDefinition = "varchar(50) comment '用户父级ID'")
	public String getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	@Column(columnDefinition = "int(2) comment '层级关系'")
	public Integer getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(Integer userlevel) {
		this.userlevel = userlevel;
	}

	@Column(columnDefinition = "varchar(10) comment '管理员类型'")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id", referencedColumnName = "id") })
	public Set<Role> getRoles() {
		return roles;
	}

	@JsonBackReference
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}




	@Column(columnDefinition = "DATETIME comment '最近密码修改时间'")
	public Date getPwdUpdateTime() {
		return pwdUpdateTime;
	}

	public void setPwdUpdateTime(Date pwdUpdateTime) {
		this.pwdUpdateTime = pwdUpdateTime;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(password, user.password) &&
				Objects.equals(role, user.role) &&
				Objects.equals(nickName, user.nickName) &&
				Objects.equals(organization, user.organization) &&
				Objects.equals(lastLoginTime, user.lastLoginTime) &&
				Objects.equals(loginTimes, user.loginTimes) &&
				Objects.equals(enable, user.enable) &&
				Objects.equals(enableIPBinding, user.enableIPBinding) &&
				Objects.equals(ipAddr, user.ipAddr) &&
				Objects.equals(userId, user.userId) &&
				Objects.equals(parentUserId, user.parentUserId) &&
				Objects.equals(userlevel, user.userlevel) &&
				Objects.equals(userType, user.userType) &&
				Objects.equals(roles, user.roles) &&
				Objects.equals(pwdUpdateTime, user.pwdUpdateTime);
	}

	@Override
	public int hashCode() {

		return Objects.hash(password, role, nickName, organization, lastLoginTime,
				loginTimes, enable, enableIPBinding, ipAddr, userId, parentUserId,
				userlevel, userType, roles,pwdUpdateTime);
	}
}
