/**
 * 
 */
package com.hoob.rs.sys.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.hoob.rs.constants.PropKey;

/**
 * @author Raul	
 * 2017年8月28日
 */
@Service
public class SystemInitServiceImpl implements SystemInitService {

	private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);	
	private static final String CONFIG_ADDRESS = "/opt/hoob/NE/rs/rs-m/etc/config.properties";
	private static final String SYSTEM_INIT_SQL_PATH = "/opt/hoob/NE/rs/rs-m/etc/init_sql/";
	private static final String CONFIG_CLASS_PATH = "config.properties";
	private static final Properties CMS_CONFIG = new Properties();
	
	
	@Resource
	JdbcTemplate jdbcTemplate;
	
	@Resource(name="dsTransactionManager")
	DataSourceTransactionManager dsTransactionManager;
	
	@Resource
	SchedulerJobService schedulerJobService;
	
	/**
	 * 加载cms-config.properties
	 * @return
	 */
	@Override
	public void loadSystemProperties() {
		File file = new File(CONFIG_ADDRESS);
		//备份
		Properties temp = new Properties();
		
		synchronized (CMS_CONFIG) {
			temp.putAll(CMS_CONFIG);
			//清除更新前配置
			CMS_CONFIG.clear();
			if(file.exists()){
				InputStream fis = null;
				try {
					fis = new FileInputStream(file);
					CMS_CONFIG.load(fis);
				} catch (IOException e) {				
					logger.error(e);
					//恢复
					CMS_CONFIG.putAll(temp);
				} finally {
					if (null != fis) {
						try {
							fis.close();
						} catch (IOException e) {
							logger.debug(e.getMessage(), e);
						}
					}
				}
			}else{
				InputStream is = null;
				try {
					is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_CLASS_PATH);
					CMS_CONFIG.load(is);
				} catch (IOException e) {
					logger.error(e);
					//恢复
					CMS_CONFIG.putAll(temp);
				} finally {
					if (null != is) {
						try {
							is.close();
						} catch (IOException e) {
							logger.debug(e.getMessage(), e);
						}
					}
				}
			}	
		}		
		//销毁临时配置
		temp.clear();
		temp = null;
		
	}
	
	
	
	@Override
	public void initSystemDB(){
		File initSystemSqlPath = new File(SYSTEM_INIT_SQL_PATH);
		if(initSystemSqlPath.exists()&&initSystemSqlPath.isDirectory()){
			File[] sqlFiles =  initSystemSqlPath.listFiles();
			if(null==sqlFiles || sqlFiles.length==0) {
                return;
            }
			
			for(final File file : sqlFiles){
				if(file.isFile()&&file.getName().endsWith(".sql")){
					final String[] sqls = this.getSqlList(file);
					if(null==sqls || sqls.length==0) {
                        continue;
                    }
					TransactionTemplate tt = new TransactionTemplate(dsTransactionManager);
					tt.execute(new TransactionCallbackWithoutResult() {
						@Override
						public void doInTransactionWithoutResult(TransactionStatus arg0) {
							try{								
								jdbcTemplate.batchUpdate(sqls);
								File target = new File(file.getAbsolutePath() + ".ok");
								if (target.exists()) {
									target.delete();
								}
								// 执行完毕改sql名字
								file.renameTo(target);	
							}catch(Exception ex){								
								arg0.setRollbackOnly();
								logger.error(ex.getMessage(),ex);	
							}	
						}
					});								
				}
			}
		}
	}
	
	
	
	/**
	 * 读取指定的文件，返因sql语句的集合
	 * 
	 * @param file
	 * @return <li>sql语句仅支持单行整行注释，可采用#、-- （单行双长划线）两种注释风格，双长划线后必须有空格，不支持多行及行尾注释
	 *         <li>sql语句支持换行，且sql语句要以分号结尾，分号必须在行尾
	 */
	private String[] getSqlList(File file) {		
		if (!file.exists() || !file.isFile()) {
			logger.error("file: " + file.getAbsolutePath()+File.separator +file.getName() + " not exist");
			return null;
		}
		logger.debug("load file:" + file.getAbsolutePath()+File.separator +file.getName());
		BufferedReader reader = null;		
		StringBuilder sqls = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));		
			String tempLine = null;
			// int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempLine = reader.readLine()) != null) {
				if (tempLine != null && !"".equals(tempLine.trim())
						&& !tempLine.trim().startsWith("#")
						&& !tempLine.trim().startsWith("--")) {					
					sqls.append(tempLine);
					// 如果不以分号结尾，认为需要换行
					if (!tempLine.trim().endsWith(";")){						
						sqls.append("\r\n");
					}
				}
			}			
			reader.close();
		} catch (IOException e) {
			logger.error(e);			
		} finally {			
			try {
				if (reader != null) {
                    reader.close();
                }
			} catch (IOException e) {
				logger.error(e);	
			}				
		}
		if(sqls.toString().trim().isEmpty()) return new String[]{};		
		return sqls.toString().trim().split("\\;");
	}
	
			
	/*****
	 * 初始化定时器
	 * ******/
	@Override
	public void initTimer() {
		schedulerJobService.initScheduleJob();
	}
	
	/**
	 * 根据键值获取配置值
	 * @param propKey
	 * @return
	 */
	public static final String getConfigPropValue(PropKey propKey){
		synchronized (CMS_CONFIG) {
			return CMS_CONFIG.getProperty(propKey.getKey());
		}

	}

}
