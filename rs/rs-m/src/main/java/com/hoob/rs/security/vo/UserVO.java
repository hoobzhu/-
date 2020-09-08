package com.hoob.rs.security.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;

/**
 * @author mayjors
 * 2017年9月4日
 */
public class UserVO{

	private long id;
	private String name;
	private String password;
	private String role;//用来区分 syadmin sp cp
	private String nickName;//昵称
	private String organization;//所属机构
	private Date lastLoginTime;//最近登录时间
	private Integer loginTimes = 0;//登录次数
	private Boolean enable = true;//是否启用
	private Boolean enableIPBinding = false; // 是否启用IP绑定
	private String ipAddr;
	private String userId; //用户ID
	private String parentUserId; //父级ID
	private Integer userlevel = 1; //层级 关系
	private String userType; // 超级管理员    管理员    用户 sysadmin admin user
	private Boolean pwFlag = false;	//是否重置密码

	@JsonSerialize
	private List<RoleVO> roles;
	private String roleName;

	private String cpId;
	private String spId;

	private Date createTime;
	private Date updateTime;
	/****/
	public UserVO() {

	}
	/****/
	public UserVO(List<RoleVO> roles) {
		this.roles = roles;
	}
	/****/
	public UserVO(long id, String userId) {
		this.id = id;
		this.userId = userId;
	}
	/****/
	public UserVO(long id, String userId, String cpId, String spId) {
		this.id = id;
		this.userId = userId;
		this.cpId = cpId;
		this.spId = spId;
	}

	/****/
	public UserVO(long id, String name, String role, String nickName, Date lastLoginTime, 
			Integer loginTimes, Boolean enable,
			String userId, String parentUserId, Integer userlevel,
			String userType, String cpId, String spId, Date createTime, Date updateTime) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.nickName = nickName;
		this.lastLoginTime = lastLoginTime;
		this.loginTimes = loginTimes;
		this.enable = enable;
		this.userId = userId;
		this.parentUserId = parentUserId;
		this.userlevel = userlevel;
		this.userType = userType;
		this.cpId = cpId;
		this.spId = spId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	/****/
	public UserVO(long id, String name, String role, String nickName, Date lastLoginTime,
			Integer loginTimes, Boolean enable,List<RoleVO> roles,
			String userId, String parentUserId, Integer userlevel, String userType,
			String cpId, String spId, Date createTime, Date updateTime) {
		this.id = id;
		this.name = name;
		this.role = role;
		this.nickName = nickName;
		this.lastLoginTime = lastLoginTime;
		this.loginTimes = loginTimes;
		this.enable = enable;
		this.roles = roles;
		this.userId = userId;
		this.parentUserId = parentUserId;
		this.userlevel = userlevel;
		this.userType = userType;
		this.cpId = cpId;
		this.spId = spId;
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

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getPwFlag() {
		return pwFlag;
	}

	public void setPwFlag(Boolean pwFlag) {
		this.pwFlag = pwFlag;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getEnableIPBinding() {
		return enableIPBinding;
	}

	public void setEnableIPBinding(Boolean enableIPBinding) {
		this.enableIPBinding = enableIPBinding;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}

	public Integer getUserlevel() {
		return userlevel;
	}

	public void setUserlevel(Integer userlevel) {
		this.userlevel = userlevel;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<RoleVO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleVO> roles) {
		this.roles = roles;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getSpId() {
		return spId;
	}

	public void setSpId(String spId) {
		this.spId = spId;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	@JsonSerialize(using = CustomDateSerializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
