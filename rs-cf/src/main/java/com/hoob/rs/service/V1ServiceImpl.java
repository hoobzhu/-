/**
 * 
 */
package com.hoob.rs.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hoob.rs.dao.V1Dao;


/**
 * @Description 
 * @author hoob
 * @date 2019年11月25日下午8:23:48
 */

@Service("v1Service")
public class V1ServiceImpl implements V1Service{
	private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
	private V1Dao apiADV1Dao;

	@Override
	public String getReSimilarContent(String contentId) {
		
		return apiADV1Dao.getReSimilarContent(contentId);
	}

	@Override
	public String getReUserContent(String userId) {
		
		return apiADV1Dao.getReUserContent(userId);
	}

	@Override
	public String getReTopNContent(String vodType) {
		
		return apiADV1Dao.getReTopNContent(vodType);
	}

}
