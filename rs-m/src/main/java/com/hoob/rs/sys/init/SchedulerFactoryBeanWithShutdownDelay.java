package com.hoob.rs.sys.init;

import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * tomcat关闭时，会报定时器内存泄漏。
 * 这是Quartz的bug，在调用scheduler.shutdown(true)后，Quartz检查线程并没有等待那些worker线程被停止就结束了。
 * 重写destroy()，解决内存泄漏。
 * @author Administrator
 *
 */
public class SchedulerFactoryBeanWithShutdownDelay extends SchedulerFactoryBean {

	@Override
	public void destroy() throws SchedulerException {
		super.destroy();
		// 在调用scheduler.shutdown(true)后增加延时，等待worker线程结束。
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
