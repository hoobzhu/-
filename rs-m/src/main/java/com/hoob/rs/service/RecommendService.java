package com.hoob.rs.service;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.model.ReSimilarContent;
import com.hoob.rs.model.ReTopNContent;
import com.hoob.rs.model.ReUserContent;

public interface RecommendService extends BaseService{
	/**
	 * @Title getReSimilarContent
	 * @Description 获取内容相似性推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public QueryResult<ReSimilarContent> getReSimilarContent(String contentId,int begin,int pageSize);
	/**
	 * @Title ReUserContent
	 * @Description 获取用户行为推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public QueryResult<ReUserContent> getReUserContent(String userId,int begin,int pageSize);
	/**
	 * @Title getReTopNContent
	 * @Description 获取热门内容推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public QueryResult<ReTopNContent> getReTopNContent(int begin, int pageSize);


	public void editReSimilarContent(ReSimilarContent re);

	public void editReUserContent(ReUserContent re);

	public void editReTopNContent(ReTopNContent re);

	public void deleteReSimilarContent(long id);

	public void deleteReUserContent(long id);

	public void deleteReTopNContent(long id);
	
	public ReSimilarContent getReSimilarContent(long id);

	public ReUserContent getReUserContent(long id);

	public ReTopNContent getReTopNContent(long id);
	/**
	 * @Title getReTopNContent
	 * @Description 获取热门内容推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public ReSimilarContent getReSimilarContentByContentId(String contentId);
	
	
	/**
	 * @Title getReTopNContent
	 * @Description 获取热门内容推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public ReUserContent getReUserContentByUserId(String userId);
	
	/**
	 * @Title getReTopNContent
	 * @Description 获取热门内容推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public ReTopNContent getReTopNContentByContentId(String contentId);
}
