/**
 * 
 */
package com.hoob.rs.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 
 * @author hoob
 * @date 2019年11月25日下午8:23:34
 */
public interface V1Service {
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
	public String getReTopNContent(String vodyType);
}
