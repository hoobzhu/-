package com.hoob.rs.controller;


import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.support.json.JSONUtils;
import com.hoob.rs.service.V1Service;
import com.hoob.rs.utils.StringUtils;
import com.hoob.rs.utils.Utils;
import com.google.gson.JsonParseException;
@RestController
public class V1Controller {
	private static Logger log = LogManager.getLogger(V1Controller.class.getName());
	@Resource
	private V1Service v1Service;


	/**
	 * 获取内容相似性推荐
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/similar/recommend/list")
	public Object getReSimilarContent(	@RequestParam(value="contentid",required = true) String contentid,
		HttpServletRequest request) {
		Map<String,Object>response=Utils.initResponse();
	
		String contentIds= v1Service.getReSimilarContent(contentid);
		response.put("contentIds", contentIds);
		return response;
	}
	/**
	 * 获取用户行为推荐内容
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/user/recommend/list")
	public Object getReUserContent(	@RequestParam(value="userid",required = true) String userid,
		HttpServletRequest request) {
		Map<String,Object>response=Utils.initResponse();
		String contentIds= v1Service.getReUserContent(userid);
		response.put("contentIds", contentIds);
		return response;
	}
	/**
	 * 获取热门内容推荐
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/top/recommend/list")
	public Object getReTopNContent(@RequestParam(value="programtype",required = false)String vodType,
	HttpServletRequest request) {
		Map<String,Object>response=Utils.initResponse();
		vodType=StringUtils.handleStrParam(vodType);
		String contentIds= v1Service.getReTopNContent(vodType);
		response.put("contentIds", contentIds);
		return response;
	}
}
