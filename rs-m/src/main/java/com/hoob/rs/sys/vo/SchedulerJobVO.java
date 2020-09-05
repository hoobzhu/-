package com.hoob.rs.sys.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hoob.rs.utils.CustomDateDeserializer;

public class SchedulerJobVO {
	private long id;//主键	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date createTime;//创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date updateTime;//最后更新时间
	private String jobName;//任务名称
	private Integer jobGroup;//任务所属分组
	private Boolean status;//任务状态 :false为禁用,true为启用 
	private Integer triggerType;// 触发类型,可取值:cron、simple、calendar
	private String cronExpression;//任务运行时间表达式
	private String targetObject;//jobDetail类似全名
	private String description;  //描述
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date executTime;	 //执行时间
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Integer getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(Integer jobGroup) {
		this.jobGroup = jobGroup;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonDeserialize(using = CustomDateDeserializer.class)
	public Date getExecutTime() {
		return executTime;
	}
	public void setExecutTime(Date executTime) {
		this.executTime = executTime;
	}
}
