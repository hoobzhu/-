package com.hoob.rs.sys.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.constants.StatusCode;
import com.hoob.rs.sys.model.SchedulerJob;
import com.hoob.rs.sys.service.SchedulerJobService;
import com.hoob.rs.sys.vo.SchedulerJobResponse;
import com.hoob.rs.sys.vo.SchedulerJobVO;
import com.hoob.rs.utils.StringUtils;

@RestController
public class SchedulerJobController {
	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	@Resource
	private	SchedulerJobService schedulerJobService;

	@RequestMapping(method=RequestMethod.GET,path="/v1/sys/schdljobmng/list", produces={"application/json"})
	public SchedulerJobResponse getSchedulerJobList(@RequestParam("jobgroup") String jobgroup){
		// logger.debug("get SchedulerJob list ");
		SchedulerJobResponse response = new SchedulerJobResponse();


		Integer jobgroup_int=null;
		try {
			jobgroup_int=StringUtils.handleIntParam(jobgroup);
			QueryResult<SchedulerJobVO> schedulerJobList = schedulerJobService.getSchedulerJobs(jobgroup_int, 0,
					Integer.MAX_VALUE);
			if (null != schedulerJobList) {
				response.setTotal(schedulerJobList.getCount());
				response.setSchedulerJobList(schedulerJobList.getResults());
			}
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription("get SchedulerJob list error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.GET,path="/v1/sys/schdljobmng/detail/{id}", produces={"application/json"})
	public SchedulerJobResponse getSchedulerJob(@PathVariable("id") String id){
		// logger.debug("get SchedulerJob  ");
		SchedulerJobResponse response = new SchedulerJobResponse();


		Long idInt=null;
		try {
			idInt=StringUtils.handleLongParam(id);
			SchedulerJobVO schedulerJobVO=schedulerJobService.getSchedulerJobVOByID(idInt);
			response.setSchedulerJobVO(schedulerJobVO);
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription("get SchedulerJob  error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.POST,path="/v1/sys/schdljobmng/enable/{id}", produces={"application/json"})
	public SchedulerJobResponse toEnable(@PathVariable("id") String id){
		// logger.debug("SchedulerJob toEnable  ");
		SchedulerJobResponse response = new SchedulerJobResponse();


		Long idInt=null;
		try {
			idInt=StringUtils.handleLongParam(id);
			schedulerJobService.toEnable(idInt);
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription(" SchedulerJob toEnable error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.PUT,path="/v1/sys/schdljobmng/edit",consumes={"application/json"}, produces={"application/json"})
	public SchedulerJobResponse updateScheduler(@RequestBody SchedulerJobVO schedulerJobVO){
		// logger.debug("updateScheduler  ");
		SchedulerJobResponse response = new SchedulerJobResponse();

		try {
			SchedulerJob schedulerJob=schedulerJobService.getSchedulerJobByID(schedulerJobVO.getId());
			if(schedulerJob==null){
				response.setResultCode(-1);
				response.setDescription(" SchedulerJob is null");
			}
			else{
				/*schedulerJobVO.setCreateTime(schedulerJob.getCreateTime());
        		schedulerJobVO.setTargetObject(schedulerJob.getTargetObject());
        		schedulerJobVO.setJobGroup(schedulerJob.getJobGroup());;
        		schedulerJobVO.setId(schedulerJob.getId());
        		schedulerJobVO.setUpdateTime(new Date());
        		schedulerJobVO.setStatus(schedulerJob.getStatus());
        		schedulerJobVO.setJobName(schedulerJob.getJobName());
        		schedulerJobVO.setTriggerType(schedulerJob.getTriggerType());*/
				schedulerJob.setCronExpression(schedulerJobVO.getCronExpression());
				schedulerJob.setDescription(schedulerJobVO.getDescription());
				schedulerJob.setJobGroup(schedulerJobVO.getJobGroup());
				schedulerJob.setStatus(schedulerJobVO.getStatus());
				schedulerJobService.update(schedulerJob);
			}
		} catch (Exception e) {
			response.setResultCode(StatusCode.UI.UI_1);
			response.setDescription(" updateScheduler error");
			logger.debug(e.getMessage(),e);
		}

		return response;
	}
	@RequestMapping(method=RequestMethod.POST,path="/v1/sys/schdljobmng/bathenable", produces={"application/json"})
	public SchedulerJobResponse bathEnable(
			@RequestParam("ids") List<Long> ids,
			@RequestParam("status") int status ){
		SchedulerJobResponse response = new SchedulerJobResponse();
	
			for(Long id:ids){
				SchedulerJob sch=schedulerJobService.getSchedulerJobByID(id);
				if(sch!=null){
					try {
						if(status==1){
							//批量启用
							sch.setStatus(true);
							schedulerJobService.start(sch);
						}
						else{
							//批量关闭
							sch.setStatus(false);
							schedulerJobService.stop(sch);
						}
					
					} catch (Exception e) {
						sch.setStatus(false);
						response.setResultCode(StatusCode.UI.UI_1);
						response.setDescription("bathEnable  is error");
					    logger.error(e.getMessage(),e);
					}
				}
			}
		

		return response;
	}
}
