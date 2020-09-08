/**
 * 
 */
package com.hoob.rs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hoob.rs.dao.V1Dao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hoob.rs.dao.V1Dao;
import com.hoob.rs.model.ReSimilarContent;
import com.hoob.rs.model.ReUserContent;


/**
 * @Description 
 * @author hoob
 * @date 2019年11月25日下午8:23:48
 */

@Service("v1Service")
public class V1ServiceImpl implements V1Service{
	private static final Logger LOGGER = LogManager.getLogger(V1ServiceImpl.class);
	private static Map<Integer,String>TOPMAP=new ConcurrentHashMap<Integer,String>();
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

	@Override
	public void reLoadCache() {
		//0.获取全量内容前20
		//1.电影前10,连续剧前10
		//2.获取电影10，连续剧5个,综艺5个
		//3.获取电影5，连续剧10个，综艺5个
		//4.获取电影5，连续剧10个，少儿5个
		//5.获取电影5，连续剧5个，综艺10个
		//6.获取电影5，连续剧5个，综艺5个，少儿5个
        //7.获取电影5，连续剧5个，少儿10个
		//8.连续剧5个，综艺10个，少儿5个
		//9.电影10个，综艺5个，少儿5个
		String allTop20=apiADV1Dao.getReTopNContent(null);
		String sql="select GROUP_CONCAT(contentId) as contentIds from ( select contentId  "
				+ "from topn_recommendation where programType=1000 order by playCount desc limit 10 )tmp";
		String movieTop10=apiADV1Dao.getBySQL(sql);
		sql="select GROUP_CONCAT(contentId) as contentIds from ( select contentId  "
				+ "from topn_recommendation where programType=1001 order by playCount desc limit 10 )tmp";
		String seriesTop10=apiADV1Dao.getBySQL(sql);
	    sql="select GROUP_CONCAT(contentId) as contentIds from ( select contentId  "
				+ "from topn_recommendation where programType=1000 order by playCount desc limit 5 )tmp";
		String movieTop5=apiADV1Dao.getBySQL(sql);
		sql="select GROUP_CONCAT(contentId) as contentIds from ( select contentId  "
				+ "from topn_recommendation where programType=1001 order by playCount desc limit 5 )tmp";
		String seriesTop5=apiADV1Dao.getBySQL(sql);
		
	}
	

}
