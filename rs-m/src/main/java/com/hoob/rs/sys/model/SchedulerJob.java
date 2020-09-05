package com.hoob.rs.sys.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.hoob.rs.comm.model.BaseEntity;

@Entity
@Table(name = "schedulerjob", indexes = { @Index(columnList = "jobName", unique = true) })
public class SchedulerJob extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jobName;//任务名称
	private Integer jobGroup;//任务所属分组
	private Boolean status;//任务状态 :false为禁用,true为启用 
	private Integer triggerType;// 触发类型,可取值:cron、simple、calendar
	private String cronExpression;//任务运行时间表达式
	private String targetObject;//jobDetail类似全名
	private String description;  //描述
	private Date executTime;	 //执行时间
	
	@Column(nullable = false, unique = true, columnDefinition = "varchar(50) comment '任务名称'")
	public String getJobName() {
		return jobName;
	}
	@Column(columnDefinition="int default 1  comment '1:cms分发  2：cdn 分发  3 :bms分发 4:epg  5  TVGW&TCGS 6：c2注入   7：c1注入  8：c3注入   9：其他'")
	public Integer getJobGroup() {
		return jobGroup;
	}
	@Column(columnDefinition="bit(1) default 0 comment '启用状态'")
	public Boolean getStatus() {
		return status;
	}
	@Column(columnDefinition="int default 1 comment '定时器类型  1：cron、2：simple:3：calendar'")
	public Integer getTriggerType() {
		return triggerType;
	}
	@Column(nullable = false,columnDefinition="varchar(255) comment '定时器表达式'")
	public String getCronExpression() {
		return cronExpression;
	}
	@Column(nullable = false,unique=true,columnDefinition="varchar(255) comment '定时器全名'")
	public String getTargetObject() {
		return targetObject;
	}
	@Column(columnDefinition="varchar(255) comment '描述'")
	public String getDescription() {
		return description;
	}
	@Column(columnDefinition="DATETIME comment '执行时间'")	
	public Date getExecutTime() {
		return executTime;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public void setJobGroup(Integer jobGroup) {
		this.jobGroup = jobGroup;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setExecutTime(Date executTime) {
		this.executTime = executTime;
	}
}
