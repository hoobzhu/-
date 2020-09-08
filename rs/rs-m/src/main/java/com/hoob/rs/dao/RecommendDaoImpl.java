package com.hoob.rs.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.model.ReSimilarContent;
import com.hoob.rs.model.ReTopNContent;
import com.hoob.rs.model.ReUserContent;
import com.hoob.rs.utils.StringUtils;

@Repository("recommendDao")
public class RecommendDaoImpl extends BaseDaoImpl implements RecommendDao{

	@Override
	public QueryResult<ReSimilarContent> getReSimilarContent(String contentId,int begin, int pageSize) {

		HashMap<String, Object> conditions=new HashMap<String,Object>();
		StringBuffer jpql = new StringBuffer("SELECT s from  ReSimilarContent s ");
		if(StringUtils.isNotEmpty(contentId)){
			jpql.append(" where s.contentId=:contentId");
			conditions.put("contentId", contentId);
		}
		jpql.append(" order by s.id asc ");

		return query(jpql, conditions, begin, pageSize);
	}

	@Override
	public QueryResult<ReUserContent> getReUserContent(String userId, int begin,int pageSize) {
		HashMap<String, Object> conditions=new HashMap<String,Object>();
		StringBuffer jpql = new StringBuffer("select re from  ReUserContent re ");
		if(StringUtils.isNotEmpty(userId)){
			jpql.append(" where re.userId=:userId");
			conditions.put("userId", userId);
		}
		jpql.append(" order by re.id asc ");

		return query(jpql, conditions, begin, pageSize);
	}

	@Override
	public QueryResult<ReTopNContent> getReTopNContent(int begin, int pageSize) {
		StringBuffer jpql = new StringBuffer("select re from  ReTopNContent re order by re.playCount desc ");
		return query(jpql,null,begin,pageSize);
	}

	@Override
	public ReSimilarContent getReSimilarContentByContentId(String contentId) {
		HashMap<String, Object> conditions=new HashMap<String,Object>();
		StringBuffer jpql = new StringBuffer("select re from  ReSimilarContent re ");
		jpql.append(" where re.contentId=:contentId");
		conditions.put("contentId", contentId);
		return this.findOne(jpql.toString(), conditions);
	}

	@Override
	public ReUserContent getReUserContentByUserId(String userId) {
		HashMap<String, Object> conditions=new HashMap<String,Object>();
		StringBuffer jpql = new StringBuffer("select re from  ReUserContent re ");
		jpql.append(" where re.userId=:userId");
		conditions.put("userId", userId);
		return this.findOne(jpql.toString(), conditions);

	}

	@Override
	public ReTopNContent getReTopNContentByContentId(String contentId) {
		HashMap<String, Object> conditions=new HashMap<String,Object>();
		StringBuffer jpql = new StringBuffer("select re from  ReTopNContent re ");
		jpql.append(" where re.contentId=:contentId");
		conditions.put("contentId", contentId);
		return this.findOne(jpql.toString(), conditions);
	}

}
