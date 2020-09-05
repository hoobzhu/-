package com.hoob.rs.sys.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.sys.constant.SchedulerJobType;
import com.hoob.rs.sys.dao.SchedulerJobDao;
import com.hoob.rs.sys.model.SchedulerJob;
import com.hoob.rs.sys.vo.SchedulerJobVO;

@Service("schedulerJobService")
@Transactional
public class SchedulerJobServiceImpl implements SchedulerJobService {
	Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	@Resource
	private Scheduler scheduler;
	@Resource
	private SchedulerJobDao schedulerJobDao;

	/**
	 * 更新定时器
	 * 
	 * @param SchedulerJobVO
	 *            schedulerJobVO return
	 */
	@Override
	public SchedulerJobVO update(SchedulerJobVO schedulerJobVO) {
		if (schedulerJobVO != null) {
			SchedulerJob schedulerJob = new SchedulerJob();
			BeanUtils.copyProperties(schedulerJobVO, schedulerJob);
			update(schedulerJob);
			return schedulerJobVO;
		}
		return null;
	}

	/**
	 * 更新定时器
	 * 
	 * @param SchedulerJob
	 *            schedulerJob return
	 */
	@Override
	public SchedulerJob update(SchedulerJob schedulerJob) {
		// 检查定时器的状态是停止还是开启，对定时器进行对应的
		try {
			schedulerJob = schedulerJobDao.update(schedulerJob);
			if (schedulerJob.getStatus()) {
				// 启动定时器
				start(schedulerJob);
			} else {
				// 关闭定时器
				stop(schedulerJob);
			}
			return schedulerJob;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 更新定时器的起停状态
	 * 
	 * @param Serializable
	 *            entityId return
	 */
	@Override
	public Boolean toEnable(Serializable entityId) {
		Boolean flag = false;
		if (entityId != null) {
			SchedulerJob schedulerJob = schedulerJobDao.find(SchedulerJob.class, entityId);
			if (schedulerJob != null) {
				schedulerJob.setStatus(!schedulerJob.getStatus());
				try {
					if (schedulerJob.getStatus()) {
						if (start(schedulerJob)) {
							schedulerJobDao.update(schedulerJob);
							flag = true;
						}
					} else {
						if (stop(schedulerJob)) {
							schedulerJobDao.update(schedulerJob);
							flag = true;
						}
					}
				} catch (Exception e) {
					schedulerJob.setStatus(!schedulerJob.getStatus());
					logger.error(e.getMessage(), e);
				}

			}
		}
		return flag;
	}

	/**
	 * 根据定时器ID 获取单个的定时VO对象
	 * 
	 * @param Serializable
	 *            entityId return
	 */
	@Override
	public SchedulerJobVO getSchedulerJobVOByID(Serializable entityId) {
		if (entityId != null) {
			SchedulerJob schedulerJob = getSchedulerJobByID(entityId);
			SchedulerJobVO schedulerJobVO = new SchedulerJobVO();
			BeanUtils.copyProperties(schedulerJob, schedulerJobVO);
			return schedulerJobVO;
			// return schedulerJobDao.find(SchedulerJobVO.class, entityId);
		}
		return null;
	}

	/**
	 * 根据定时器ID 获取单个的定时
	 * 
	 * @param Serializable
	 *            entityId return
	 */
	@Override
	public SchedulerJob getSchedulerJobByID(Serializable entityId) {

		if (entityId != null) {
			return schedulerJobDao.find(SchedulerJob.class, entityId);

		}
		return null;
	}

	/**
	 * 停止job
	 * 
	 * @param job
	 *            return boolean 备注：目前只支持cron类型的job
	 * @throws SchedulerException
	 */
	@Override
    public boolean stop(SchedulerJob job) throws SchedulerException {
		if (null == job) {
			logger.error("job is null");
			return false;
		}
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), String.valueOf(job.getJobGroup()));
		Trigger trigger = scheduler.getTrigger(triggerKey);
		if (SchedulerJobType.CRON.getValue() == job.getTriggerType()) {
			if (null != trigger) {
				scheduler.deleteJob(trigger.getJobKey());
				logger.info("stop job  successfully");
			} else {
				logger.warn("can not get tigger cron  by jobName(" + job.getJobName() + ") and jobGroup("
						+ job.getJobGroup() + ")");
			}
			return true;
		} else {
			logger.error("unsuport trigger stop job(" + job.toString() + ") fail");
		}

		return false;
	}

	/**
	 * 启动job
	 * 
	 * @param job
	 *            备注：目前只支持cron类型的job
	 * @throws SchedulerException
	 *             如果原定时器存在则更新，否则新建
	 * @throws ClassNotFoundException
	 */
	@Override
    public boolean start(SchedulerJob job) throws SchedulerException, ClassNotFoundException {
		if (null == job) {
            return false;
        }

		// 如果job的状态为禁用，则不启动
		if (job.getStatus() == false) {
			logger.info("schedule job status is disabled,can not need to start");
			return true;
		}

		if (job.getTriggerType() == SchedulerJobType.CRON.getValue()) {
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), String.valueOf(job.getJobGroup()));
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在，创建一个
			if (null == trigger) {
				String className = job.getTargetObject();
				@SuppressWarnings("unchecked")
				Class<Job> cl = (Class<Job>) Class.forName(className);

				JobDetail jobDetail = JobBuilder.newJob(cl)
						.withIdentity(job.getJobName(), String.valueOf(job.getJobGroup())).build();
				jobDetail.getJobDataMap().put("scheduleJob", job);

				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), String.valueOf(job.getJobGroup()))
						.withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
				logger.info("job(" + job.toString() + ") started success");
			} else {// Trigger已存在，那么更新相应的定时设置
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
				logger.info("job(" + job.toString() + ") is exist,now update is and restarted success");
			}
			return true;
		} else {
			logger.error("unsuport trigger start job(" + job.toString() + ") fail");
		}
		return false;
	}

	@Override
	public void initScheduleJob() {
		// 从db中查询所有任务
		QueryResult<SchedulerJob> scheduleJobList = schedulerJobDao.getSchedulerJobs(true);
		if (null != scheduleJobList && scheduleJobList.getResults() != null
				&& scheduleJobList.getResults().size() > 0) {
			for (SchedulerJob job : scheduleJobList.getResults()) {
				try {
					//stop(job);
					start(job);// 启动定时器
				} catch (SchedulerException e) {
					logger.error("job(" + job.toString() + ") start fail");
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					logger.error("job(" + job.toString() + ") start fail");
					e.printStackTrace();
				}
			}
		} else {
			logger.warn("scheduleJobList is null or empty,none timer start");
		}

	}

	@Override
	public QueryResult<SchedulerJobVO> getSchedulerJobs(Integer group, Integer fisrt, Integer max) {
		QueryResult<SchedulerJob> queryResult = schedulerJobDao.getSchedulerJobs(group, fisrt, max);
		if(null==queryResult) {
            return null;
        }
		if(null==queryResult.getResults()) {
            return null;
        }
		QueryResult<SchedulerJobVO> result = new QueryResult<SchedulerJobVO>();
		List<SchedulerJobVO> list = new ArrayList<SchedulerJobVO>();
		for(SchedulerJob job : queryResult.getResults()){
			SchedulerJobVO vo = new SchedulerJobVO();
			BeanUtils.copyProperties(job, vo);
			list.add(vo);
		}
		result.setResults(list);
		result.setCount(queryResult.getCount());
		return result;
	}

}
