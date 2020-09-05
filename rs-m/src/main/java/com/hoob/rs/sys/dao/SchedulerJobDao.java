package com.hoob.rs.sys.dao;

import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.sys.model.SchedulerJob;

public interface SchedulerJobDao extends BaseDao {
	
	public SchedulerJob update(SchedulerJob schedulerJob);
	

	public QueryResult<SchedulerJob> getSchedulerJobs(Boolean status);
	
	/**
	 *  根据定时器分组   获取定时VO列表
	 *  @param Integer group 组别
	 *  @param Integer fisrt 起始数 
		@param Integer max  每页最大数量 
	 *  return 
	 */
	public QueryResult<SchedulerJob> getSchedulerJobs(Integer group,Integer fisrt,Integer max);
}
