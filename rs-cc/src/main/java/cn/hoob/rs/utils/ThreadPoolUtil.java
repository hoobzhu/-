/**
 * 
 */
package cn.hoob.rs.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * @Description 
 * @author hoob
 * @date 2019年7月1日上午11:13:47
 */
public class ThreadPoolUtil {
	private static int corePoolSize = 50;
	private static final  ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
			corePoolSize,
	        new BasicThreadFactory.Builder().namingPattern("thread-pool-util-%d").daemon(true).build()); 
	/***/
	public static void execute(Runnable runnable){
		executorService.execute(runnable);
	}	
	
}
