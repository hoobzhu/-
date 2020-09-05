package com.hoob.rs.sys.vo;

import java.io.Serializable;
import java.util.Date;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hoob.rs.utils.CustomDateDeserializer;

/**
 * 操作日志VO封装类
 * 
 * @author River
 * @date 2017年9月1日
 */
public class OperlogVO implements Serializable {

	private static final long serialVersionUID = 7688327431659181047L;

	private long id;	
	
	// 操作类型：add-添加，edit-更新，delete-删除，login-登录
	private String operateType;

	// 请求内容
	private String content;

	// 操作对象
	private String operateObj;

	// 操作状态：0 成功，-1 失败，-2 用户不存在，-3 用户被禁用，-4 无可用contentId，-5 验证码错误，-6 IP错误
	private Integer operateStatus;

	// 操作人
	private String operatorName;

	// 用户IP
	private String operatorIP;

	// 详细
	private String detail;

	// 描述
	private String description;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;
	public OperlogVO(){super();}
	
	public OperlogVO(long id,Date createTime,String content,String description,String detail,String operateObj,int operateStatus,String operateType,String operatorIP,String operatorName){
		super();
		this.id=id;
		this.createTime=createTime;
		this.content=content;
		this.description=description;
		this.detail=detail;
		this.operateObj=operateObj;
		this.operateStatus=operateStatus;
		this.operateType=operateType;
		this.operatorIP=operatorIP;
		this.operatorName=operatorName;
	}
	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperateObj() {
		return operateObj;
	}

	public void setOperateObj(String operateObj) {
		this.operateObj = operateObj;
	}

	public Integer getOperateStatus() {
		return operateStatus;
	}

	public void setOperateStatus(Integer operateStatus) {
		this.operateStatus = operateStatus;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorIP() {
		return operatorIP;
	}

	public void setOperatorIP(String operatorIP) {
		this.operatorIP = operatorIP;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCreateTime() {
		return createTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
