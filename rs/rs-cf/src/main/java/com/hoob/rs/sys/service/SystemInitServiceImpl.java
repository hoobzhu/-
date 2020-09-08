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

	private static final Logger LOGGER = LogManager.getLogger(SystemInitServiceImpl.class);	
	private static final String CONFIG_ADDRESS = "/opt/fonsview/NE/rs/rs-m/etc/config.properties";
	private static final String CONFIG_CLASS_PATH = "config.properties";
	private static final Properties CMS_CONFIG = new Properties();
	
	
	@Resource
	JdbcTemplate jdbcTemplate;
	
	@Resource(name="dsTransactionManager")
	DataSourceTransactionManager dsTransactionManager;
	
	
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
					LOGGER.error(e);
					//恢复
					CMS_CONFIG.putAll(temp);
				} finally {
					if (null != fis) {
						try {
							fis.close();
						} catch (IOException e) {
							LOGGER.debug(e.getMessage(), e);
						}
					}
				}
			}else{
				InputStream is = null;
				try {
					is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_CLASS_PATH);
					CMS_CONFIG.load(is);
				} catch (IOException e) {
					LOGGER.error(e);
					//恢复
					CMS_CONFIG.putAll(temp);
				} finally {
					if (null != is) {
						try {
							is.close();
						} catch (IOException e) {
							LOGGER.debug(e.getMessage(), e);
						}
					}
				}
			}	
		}		
		//销毁临时配置
		temp.clear();
		temp = null;
		
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
