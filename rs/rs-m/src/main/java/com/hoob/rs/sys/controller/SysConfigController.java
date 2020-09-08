package com.hoob.rs.sys.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hoob.rs.sys.constant.ConfigKey;
import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.sys.vo.SysConfigVO;
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
import com.hoob.rs.comm.vo.ListResponse;
import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.comm.vo.VoResponse;
import com.hoob.rs.constants.StatusCode;
import com.hoob.rs.sys.constant.ConfigKey;
import com.hoob.rs.sys.model.SysConfig;
import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.sys.vo.SysConfigVO;
import com.hoob.rs.utils.JsonUtils;
import com.hoob.rs.utils.StringUtils;
import com.google.gson.JsonParseException;
@RestController
public class SysConfigController {
	private static Logger log = LogManager.getLogger(SysConfigController.class.getName());
	@Resource
	private SysConfigService sysConfigService;


	/**
	 * 获取系统配置列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/list")
	public ListResponse getList(HttpServletRequest request) {
		// log.debug("get config list ");
		ListResponse response = new ListResponse(0);

		long total = 0;
		try {

			List<SysConfigVO> sysConfigList = sysConfigService.getConfigList();
			if (null != sysConfigList && !sysConfigList.isEmpty()) {
				response.setList(sysConfigList);
				response.setTotal(0);// 该查询不显示总数，置为0只是占位
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new ListResponse(-1);
		}

		return response;
	}

	/**
	 * 获取系统常量列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/constantlist")
	public ListResponse getContantList(HttpServletRequest request) {
		// log.debug("get config list ");
		ListResponse response = new ListResponse(0);

		long total = 0;
		try {

			List<SysConfigVO> sysConfigList = sysConfigService.getConstantList();
			if (null != sysConfigList && !sysConfigList.isEmpty()) {
				response.setList(sysConfigList);
				response.setTotal(0);// 该查询不显示总数，置为0只是占位
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new ListResponse(-1);
		}

		return response;
	}
	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/detail")
	public VoResponse get(@RequestParam("id") String id, HttpServletRequest request) {
		// log.debug("get config ");
		VoResponse response = new VoResponse(0);
		long idLong = StringUtils.handleLongParam(id);
		SysConfigVO sysConfigVo = null;
		try {
			if (id != null) {
				sysConfigVo = sysConfigService.findConfig(idLong);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}

		response.setVo(sysConfigVo);
		return response;
	}

	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/bykey")
	public VoResponse<SysConfigVO> getConfigByKey(@RequestParam("key") String key) {
		// log.debug("get config ");
		VoResponse<SysConfigVO> response = new VoResponse<SysConfigVO>(0);
		key=StringUtils.handleStrParam(key);
		SysConfigVO sysConfigVo = null;
		try {
			if (StringUtils.isNotEmpty(key)) {
				SysConfig	sysConfig = sysConfigService.find(key);
				if(sysConfig!=null){
					sysConfigVo=new SysConfigVO();
					BeanUtils.copyProperties(sysConfig, sysConfigVo);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
		}

		response.setVo(sysConfigVo);
		return response;
	}
	/**
	 * 更新配置内容
	 * 
	 * @param ftpServerVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/v1/sys/config/update", consumes = {
			"application/json" }, produces = { "application/json" })
	public VoResponse update(@RequestBody SysConfigVO sysConfigVo) {
		// log.debug("update ftp ");
		VoResponse response = new VoResponse(0);
		try {
			sysConfigService.updateSysConfig(sysConfigVo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		return response;
	}

	/**
	 * 刷新缓存
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/v1/sys/config/refresh")
	public VoResponse refresh(HttpServletRequest request) {
		// log.debug("refresh ");
		VoResponse response = new VoResponse(0);
		try {
			sysConfigService.reloadCache();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		return response;
	}

	/**
	 * 启停配置
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/v1/sys/config/enable")
	public VoResponse enableConfig(@RequestParam(value = "id", required = false) String configId) {
		// log.debug("enable ");
		VoResponse response = new VoResponse(0);
		long idLong = StringUtils.handleLongParam(configId);
		try {
			sysConfigService.enableConfig(idLong);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}
		return response;
	}

	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/language")
	public VoResponse getLanguage() {
		log.debug("get language ");
		VoResponse response = new VoResponse(0);
		SysConfig sysConfig = null;
		try {
			sysConfig = sysConfigService.find(ConfigKey.LANGS_CONFIG);
			if (sysConfig == null) {
				sysConfigService.reloadCache();
				sysConfig = sysConfigService.find(ConfigKey.LANGS_CONFIG);
			}
			JSONUtils.parse(sysConfig.getValue());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(-1);
		}

		response.setVo(JSONUtils.parse(sysConfig.getValue()));
		return response;
	}

	
	
	
	/**
	 * 获取配置详情
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(method = RequestMethod.GET, path = "/v1/sys/config/value")
	public VoResponse getValue(@RequestParam(value = "key", required = true) String keyStr) {
		log.debug("get value ");
		VoResponse response = new VoResponse(0);
		SysConfig sysConfig = null;

		sysConfig = sysConfigService.find(keyStr.toUpperCase());
		if (null != sysConfig) {
			try {
			
				response.setVo(JSONUtils.parse(sysConfig.getValue()));
			} catch (JsonParseException e) {
				//log.error(e.getMessage(), e);
				response.setVo(sysConfig.getValue());
			}catch(IllegalArgumentException e){
				response.setVo(sysConfig.getValue());
			}catch(Exception e){
				log.error(e.getMessage(), e);
				response = new VoResponse(-1);
			}
		}

		return response;
	}
	
	
	
	/**
	 * 获取配置详情 JSON格式 不支持非JSON 
	 * 
	 * @author Jack
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping( path = "/v1/sys/config/json",produces="application/json")
	public VoResponse getJSON(@RequestParam(value = "key", required = true) String key) {
		SysConfig sysConfig = sysConfigService.find(key);
		if (null != sysConfig) {
			VoResponse response = new VoResponse();
			response.setResultCode(StatusCode.UI.UI_0);
			response.setDescription("JSON is unusefull");
			response.setVo(JsonUtils.json2Obj(sysConfig.getValue(), Map.class));//获取配置详情 JSON格式 不支持非JSON 
		}
		
		VoResponse response = new VoResponse();
		response.setResultCode(StatusCode.UI.UI_1);
		response.setDescription("JSON is unusefull");
		return response;
	}

	/**
	 * 刷新缓存
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, path = "/v1/sys/config/clean")
	public Response clean() {
		Response response = new Response(StatusCode.UI.UI_0);
		try {
			// 清理系統配置，系統常量緩存
			sysConfigService.reloadCache();
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(StatusCode.UI.UI_1);
		}
		return response;
	}
}
