/**
 * 
 */
package com.hoob.rs.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hoob.rs.model.ReSimilarContent;
import com.hoob.rs.model.ReUserContent;

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
	
	/**
	 * @Title reLoadCache
	 * @Description 缓存热门内容作为推荐作为冷推荐的结果
	 * @param 
	 * @return 
	 * @throws 
	 */
	public void reLoadCache();
}
