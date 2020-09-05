package com.hoob.rs.sys.vo;

import java.io.Serializable;

public class OPerlogConfigVO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String operateObj;  //操作对象
	private String operateUrl;  //操作接口
	private String operateType; //操作类型   add/edit/delete
	private String operateMethod; //操作接口类型  post/put/delete/get
	private String operateDetail; //操作描述

	public String getOperateObj() {
		return operateObj;
	}
	public void setOperateObj(String operateObj) {
		this.operateObj = operateObj;
	}
	public String getOperateUrl() {
		return operateUrl;
	}
	public void setOperateUrl(String operateUrl) {
		this.operateUrl = operateUrl;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOperateDetail() {
		return operateDetail;
	}
	public void setOperateDetail(String operateDetail) {
		this.operateDetail = operateDetail;
	}
	
	public String getOperateMethod() {
		return operateMethod;
	}
	public void setOperateMethod(String operateMethod) {
		this.operateMethod = operateMethod;
	}
	
	@Override
	public String toString() {
		return "OPDomainConfig [operateObj=" + operateObj + ", operateUrl=" + operateUrl
				+ ", operateType=" + operateType + ", operateMethod=" + operateMethod + ", operateDetail="
				+ operateDetail + "]";
	}
	
}
