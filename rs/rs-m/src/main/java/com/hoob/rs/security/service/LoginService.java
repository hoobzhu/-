package com.hoob.rs.security.service;

import java.util.List;

import com.hoob.rs.security.vo.UserSession;
import com.hoob.rs.security.vo.UserSession;

/**
 * 登录服务接口
 * @author Raul	
 * 2017年9月1日
 */
public interface LoginService {

		
	/**
	 * 用户登录，并产生会话 ，如果返回值为NULL ，表示登录失败
	 * @param userId
	 * @param password
	 * @return
	 */
	public UserSession authorize(String userId, String password) throws Exception;
	
	
	
}
