package com.hoob.rs.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.dao.RecommendDao;
import com.hoob.rs.model.ReSimilarContent;
import com.hoob.rs.model.ReTopNContent;
import com.hoob.rs.model.ReUserContent;
@Service("recommendService")
@Transactional
public class RecommendServiceImpl extends BaseServiceImpl implements RecommendService{
	@Resource
	private RecommendDao recommendDao;
	
	@Override
	public QueryResult<ReSimilarContent> getReSimilarContent(String contentId,
			int begin, int pageSize) {
		return recommendDao.getReSimilarContent(contentId, begin, pageSize);
	}

	@Override
	public QueryResult<ReUserContent> getReUserContent(String userId,
			int begin, int pageSize) {
		
		return recommendDao.getReUserContent(userId, begin, pageSize);
	}

	@Override
	public QueryResult<ReTopNContent> getReTopNContent(int begin, int pageSize) {
		
		return recommendDao.getReTopNContent(begin,pageSize);
	}

	@Override
	public void editReSimilarContent(ReSimilarContent re) {
		if(re==null){
			return ;
		}
		ReSimilarContent db=recommendDao.find(ReSimilarContent.class, re.getId());
		db.setContentIds(re.getContentIds());
		db.setUpdateTime(new Date());
		recommendDao.merge(db);
		
	}

	@Override
	public void editReUserContent(ReUserContent re) {
		if(re==null){
			return ;
		}
		ReUserContent db=recommendDao.find(ReUserContent.class, re.getId());
		db.setContentIds(re.getContentIds());
		db.setUpdateTime(new Date());
		recommendDao.merge(db);
		
	}

	@Override
	public void editReTopNContent(ReTopNContent re) {
		if(re==null){
			return ;
		}
		ReTopNContent db=recommendDao.find(ReTopNContent.class, re.getId());
		db.setPlayCount(re.getPlayCount());
		db.setUpdateTime(new Date());
		recommendDao.merge(db);
		
	}

	@Override
	public void deleteReSimilarContent(long id) {
		recommendDao.remove(ReSimilarContent.class, id);
		
	}

	@Override
	public void deleteReUserContent(long id) {
		recommendDao.remove(ReUserContent.class, id);
		
	}

	@Override
	public void deleteReTopNContent(long id) {
		recommendDao.remove(ReTopNContent.class, id);
		
	}

	@Override
	public ReSimilarContent getReSimilarContent(long id) {
		ReSimilarContent db=recommendDao.find(ReSimilarContent.class,id);
		return db;
	}

	@Override
	public ReUserContent getReUserContent(long id) {
		ReUserContent db=recommendDao.find(ReUserContent.class,id);
		return db;
	}

	@Override
	public ReTopNContent getReTopNContent(long id) {
		ReTopNContent db=recommendDao.find(ReTopNContent.class,id);
		return db;
	}

	@Override
	public ReSimilarContent getReSimilarContentByContentId(String contentId) {
		return recommendDao.getReSimilarContentByContentId(contentId);
	}

	@Override
	public ReUserContent getReUserContentByUserId(String userId) {
		
		return recommendDao.getReUserContentByUserId(userId);
	}

	@Override
	public ReTopNContent getReTopNContentByContentId(String contentId) {
		
		return recommendDao.getReTopNContentByContentId(contentId);
	}

	
}
