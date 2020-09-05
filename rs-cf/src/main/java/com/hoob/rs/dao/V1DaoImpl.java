/**
 * 
 */
package com.hoob.rs.dao;

import com.hoob.rs.utils.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.*;


@Repository("v1Dao")
public class V1DaoImpl implements V1Dao {

	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
	JdbcTemplate jdbcTemplate;
	/**
	 * @Title getReSimilarContent
	 * @Description 获取内容相似性推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	@Override
	public String getReSimilarContent(String contentId) {
		StringBuilder sql= new StringBuilder();
		ArrayList<Object> params=new ArrayList<Object>();
		sql.append("select contentId,contentIds from similar_recommendation where contentId=? ");
		params.add(contentId);
		//List<ReSimilarContent> list=jdbcTemplate.query(sql.toString(),params.toArray(), new BeanPropertyRowMapper<ReSimilarContent>(ReSimilarContent.class));
        Map<String,Object>map=jdbcTemplate.queryForMap(sql.toString(),params.toArray());
		
		return map.get("contentIds")+"";
	
	}
	/**
	 * @Title getReUserContent
	 * @Description 获取用户行为推荐内容结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	@Override
	public String getReUserContent(String userId) {
		StringBuilder sql= new StringBuilder();
		ArrayList<Object> params=new ArrayList<Object>();
		sql.append("select userId,contentIds from user_recommendation where userId=? ");
		params.add(userId);
		//List<ReUserContent> list=jdbcTemplate.query(sql.toString(),params.toArray(), new BeanPropertyRowMapper<ReUserContent>(ReUserContent.class));
		//return list;
		 Map<String,Object>map=jdbcTemplate.queryForMap(sql.toString(),params.toArray());
			
		return map.get("contentIds")+"";
	}
	@Override
	public String getReTopNContent(String vodType) {
		StringBuilder sql= new StringBuilder();
		ArrayList<Object> params=new ArrayList<Object>();
		sql.append("select GROUP_CONCAT(contentId) as contentIds from topn_recommendation  ");
		if(StringUtils.isNotEmpty(vodType)){
			params.add(vodType);
			sql.append(" where programType=? ");
		}
		sql.append(" order by playCount desc");
		Map<String,Object>map=jdbcTemplate.queryForMap(sql.toString(),params.toArray());
		
		return map.get("contentIds")+"";
	}
}
