/**
 * 
 */
package com.hoob.rs.utils;

import com.hoob.rs.constants.Constants;
import com.hoob.rs.constants.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hoob.rs.constants.Constants;
import com.hoob.rs.constants.StatusCode;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 
 * @author hoob
 * @date 2018年3月8日下午12:07:20
 */
@Component 
public class Utils {
	static final Logger LOG = LogManager.getLogger(Utils.class);


	


	/**
	 * 初始化返回体
	 * @param 
	 * @return
	 */
	public static Map<String,Object> initResponse(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultCode", StatusCode.UI.UI_0);
		map.put("description", Constants.R_SUCCESS);
		return map;
	}

	/**
	 * get请求非必填参数为空时，处理为null
	 * <p>Title: processUrlNullParam</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2020年4月9日   
	 * @param obj
	 * @return
	 */
	public static String processUrlNullParam(Object obj) {
		return obj == null ? "" : obj + "";
	}
	
}
