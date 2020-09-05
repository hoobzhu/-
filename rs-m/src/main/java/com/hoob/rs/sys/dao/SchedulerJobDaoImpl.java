package com.hoob.rs.sys.dao;

import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.sys.model.SchedulerJob;

@Repository("schedulerJobDao")
public class SchedulerJobDaoImpl extends BaseDaoImpl implements SchedulerJobDao {
	Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	@Override
	public SchedulerJob update(SchedulerJob schedulerJob) {
		if(schedulerJob != null) {
			schedulerJob.setUpdateTime(new Date());
			return  super.merge(schedulerJob);
		}
		return null;
	}

	@Override
	public QueryResult<SchedulerJob> getSchedulerJobs(Boolean status) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("select s from SchedulerJob s  where 1=1 ");
		if (status != null) {
			param.put("status", status);
			jpql.append(" and s.status=:status");
		}
		return query(jpql, param, -1, -1);
	}
	
	/**
	 * 根据定时器分组 获取定时VO列表
	 * 
	 * @param Integer
	 *            group 组别
	 * @param Integer
	 *            fisrt 起始数
	 * @param Integer
	 *            max 每页最大数量 return
	 */
	@Override
	public QueryResult<SchedulerJob> getSchedulerJobs(Integer group, Integer fisrt, Integer max) {
		if (group == null || fisrt == null || max == null) {
			return null;
		}
		HashMap<String, Object> param = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder("select s from SchedulerJob s  where 1=1 ");
		if (group != null) {
			param.put("group", group);
			jpql.append(" and s.jobGroup=:group");
		}
		return query(jpql, param, fisrt, max);		
	}

}
