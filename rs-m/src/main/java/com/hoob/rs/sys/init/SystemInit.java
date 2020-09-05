/**
 * 
 */
package com.hoob.rs.sys.init;

import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.sys.service.SystemInitService;

/**
 * @author Raul	
 * 2017年8月25日
 */
@Component
public class SystemInit {
	
	private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);		
	private static final int CONFIG_RELOAD_PERIOD = 30; //30S	
	
	@Resource
	SystemInitService systemInitService; 
	
	@Resource
	SysConfigService sysConfigService;

	
	final Timer timer = new Timer();
	
	@PostConstruct	
	public void init(){
		logger.info("init start..........................");	
		//初始化配置
		systemInitService.loadSystemProperties();		
		
		//初始化系统DB
		systemInitService.initSystemDB();
		
		//初始化定时器
		//systemInitService.initTimer();		
		
		//将系统配置加载到内存
		sysConfigService.reloadCache();
		
		//周期性自动重新加载配置    60重新加載一次
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				systemInitService.loadSystemProperties();
			}
		}, CONFIG_RELOAD_PERIOD*1000,CONFIG_RELOAD_PERIOD*1000*2);
		
   
        
		logger.info("init end..........................");
	}

	
	@PreDestroy
	private void destory(){
		logger.info("------------> destory, timer cancel.");
		timer.cancel();
	}
	

	
	
	
	
}
