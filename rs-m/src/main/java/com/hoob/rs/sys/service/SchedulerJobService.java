package com.hoob.rs.sys.service;

import java.io.Serializable;

import org.quartz.SchedulerException;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.sys.model.SchedulerJob;
import com.hoob.rs.sys.vo.SchedulerJobVO;

public interface SchedulerJobService{

	/**
	 *  更新定时器
	 *  @param SchedulerJobVO schedulerJobVO
	 *  return 
	 */
	public SchedulerJobVO update(SchedulerJobVO schedulerJobVO);
	/**
	 *  更新定时器
	 *  @param SchedulerJob schedulerJob
	 *  return 
	 */
	public SchedulerJob update(SchedulerJob schedulerJob);	
	/**
	 *  更新定时器的起停状态
	 *  @param Serializable entityId
	 *  return 
	 */
	public Boolean toEnable(Serializable entityId);
	
	
	/**
	 * 根据定时器ID  获取单个的定时
	 *  @param Serializable entityId
	 *  return 
	 */
	public SchedulerJobVO getSchedulerJobVOByID(Serializable id);
	
	/**
	 * 根据定时器ID  获取单个的定时
	 *  @param Serializable entityId
	 *  return 
	 */
	public SchedulerJob getSchedulerJobByID(Serializable entityId);
	/**
	 * 停止job
	 * @param job
	 * return  boolean  
	 * 备注：目前只支持cron类型的job
	 * @throws SchedulerException 
	 */
	public boolean start(SchedulerJob sch)throws SchedulerException, ClassNotFoundException;
	/**
	 * 启动job
	 * @param job
	 * 备注：目前只支持cron类型的job
	 * @throws SchedulerException 
	 * 如果原定时器存在则更新，否则新建
	 * @throws ClassNotFoundException 
	 */
	public boolean stop(SchedulerJob sch) throws SchedulerException;

	
	/**
	 * 容器启动时初始化定时器
	 */
	public void initScheduleJob();
	
	/**
	 * 获取分组JOBS
	 * @param group
	 * @param fisrt
	 * @param max
	 * @return
	 */
	public QueryResult<SchedulerJobVO> getSchedulerJobs(Integer group,Integer fisrt,Integer max);
}
