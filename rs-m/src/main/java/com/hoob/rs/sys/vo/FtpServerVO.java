package com.hoob.rs.sys.vo;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hoob.rs.utils.CustomDateDeserializer;
import com.hoob.rs.utils.CustomDateSerializer;

/**
 * Ftp服务器
 * 
 * @author Faker
 *
 */
public class FtpServerVO {

	private long id;// id
	private String name; // ftp服务器名称
	private String hostIp; // ip地址
	private String username; // 用户名
	private String password; // 密码;
	private String rootDirectory; // 访问地址(相对路径)
	private Integer port; // 端口

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;// 创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;// 更新时间
	private boolean isLocal = false;// 是否本地服务器

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
	@JsonSerialize(using= CustomDateSerializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRootDirectory() {
		return rootDirectory;
	}

	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}

	public String getUrl() {
		return rootDirectory == null ? "" : rootDirectory;
	}

	public void setUrl(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}



	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public void check() {
		if (StringUtils.isBlank(this.hostIp)) {
			throw new IllegalArgumentException("hostIp is blank");
		}
		if (StringUtils.isBlank(this.username)) {
			throw new IllegalArgumentException("username is blank");
		}
	}

	@Override
	public String toString() {
		return "FtpPath [name=" + name + ", hostIp=" + hostIp + ", username=" + username + ", password=" + password
				+ ", rootDirectory=" + rootDirectory + ", port=" + port + "]";
	}

	@JsonIgnore
	public String getPath() {
		StringBuilder sb = new StringBuilder("ftp://");
		sb.append(this.username);
		sb.append(":");
		sb.append(this.password);
		sb.append("@");
		sb.append(this.hostIp);
		if (this.port != null) {
			sb.append(":");
			sb.append(this.port);
		}
		sb.append("/");
		return sb.toString();
	}
}
