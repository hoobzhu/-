/**
 * 
 */
package com.hoob.rs.dao;


public interface V1Dao{
	/**
	 * @Title getReSimilarContent
	 * @Description 获取内容相似性推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public String getReSimilarContent(String contentId);
	/**
	 * @Title ReUserContent
	 * @Description 获取用户行为推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public String getReUserContent(String userId);
	/**
	 * @Title getReTopNContent
	 * @Description 获取热门内容推荐结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public String getReTopNContent(String vodType);
}
