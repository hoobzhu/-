package com.fonsview.cms;

import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.quartz.SchedulerException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.QueryResult;
import com.fonsview.rs.sys.dao.SchedulerJobDao;
import com.fonsview.rs.sys.model.SchedulerJob;
import com.fonsview.rs.sys.service.SchedulerJobService;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(rollbackFor = { Exception.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false) 
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public abstract class BaseTest {	

	@Resource
	private SchedulerJobService schedulerJobService;
	
	@Resource
	private SchedulerJobDao schedulJobDao;
	
	
	@Before
	public void init() throws Exception {
		System.out.println("before");  
		
		// 从db中查询所有任务
		QueryResult<SchedulerJob> scheduleJobList = schedulJobDao.getSchedulerJobs(true);
		if (null != scheduleJobList && scheduleJobList.getCount() > 0) {
			for (SchedulerJob job : scheduleJobList.getResults()) {
				try {
					schedulerJobService.stop(job);// 启动定时器
				} catch (SchedulerException e) {					
					e.printStackTrace();
				} 
			}
		} 		
	}

	
	
	@After  
	public void close() throws Exception {  
		System.out.println("after");  
	}  

}
