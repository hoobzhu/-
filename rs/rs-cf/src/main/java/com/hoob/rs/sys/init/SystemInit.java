/**
 * 
 */
package com.hoob.rs.sys.init;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fonsview.epg.utils.Utils;
import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.sys.service.SystemInitService;

/**
 * @author Raul	
 * 2017年8月25日
 */
@Component
public class SystemInit {
	
	private static final Logger LOGGER = LogManager.getLogger(SystemInit.class);		
	private static final int CONFIG_RELOAD_PERIOD = 30; //30S	
	
	@Resource
	SystemInitService systemInitService; 
	
	@Resource
	SysConfigService sysConfigService;

	
	ScheduledExecutorService timer = new ScheduledThreadPoolExecutor(1,
			new BasicThreadFactory.Builder().namingPattern("timer-schedule-pool-%d").daemon(true).build());//配置定时器
	
	@PostConstruct	
	public void init(){
		LOGGER.info("init start..........................");	
		//初始化配置
		systemInitService.loadSystemProperties();		
		

		//将系统配置加载到内存
		sysConfigService.reloadCache();
		
		
		//周期性自动重新加载配置    30*6重新加載一次
		timer.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						systemInitService.loadSystemProperties();
						
					}
				},CONFIG_RELOAD_PERIOD*1000,CONFIG_RELOAD_PERIOD*1000*2, TimeUnit.MILLISECONDS);
        
		LOGGER.info("init end..........................");
	}

	
	@PreDestroy
	private void destory(){
		LOGGER.info("------------> destory, timer cancel.");
		timer.shutdown();;
	}
	

	
	
	
	
}
