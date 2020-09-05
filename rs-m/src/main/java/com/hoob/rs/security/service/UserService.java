package com.hoob.rs.security.service;

import java.io.Serializable;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.UserVO;

/**
 * @author mayjors
 * 2017年9月4日
 */
public interface UserService extends BaseService{
	/**
	 * 用户新增，用于前端保存
	 */
	public void addUser(UserVO userVO);	
	/**
	 * 用户新增，用于不涉及VO转换的保存
	 */
	public void addUser(User user);

	public void updateUser(User user);
	
	/**
	 * 更新用户登录信息（最近登录时间和次数）
	 * @param user
	 */
	public void updateUserLoginInfo(User user);

	public void updateUserVO(UserVO userVO);
	
	public void removeUser(Class<? extends BaseEntity> user, Serializable... entityIds);
	
	public UserVO getUserVO(Serializable entityId);

	public User getUser(Serializable entityId);

	public User getUserByUserId(String userId);

	public UserVO getUserVOByUserId(String userId);

	/**
	 * 获取当前用户以及其创建的用户
	 * @param userId
	 * @param cpid
	 * @param spid
	 * @param enable
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	public QueryResult<UserVO> getQuery(String userId, String cpid, String spid, String enable,String parentUserId, Integer firstResult, Integer maxResult);
	
	public QueryResult<User> getUsers(String userId, String cpid, String spid, String enable, Integer firstResult, Integer maxResult);

	public boolean enableUser(User user);

	public void resetPassword(User user) throws Exception;
	
}
