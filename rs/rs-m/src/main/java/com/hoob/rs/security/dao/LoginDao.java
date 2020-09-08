/**
 * 
 */
package com.hoob.rs.security.dao;

import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.User;
import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;


/**
 * @author Raul	
 * 2017年9月1日
 */
public interface LoginDao extends BaseDao {

	/**
	 * 获取用户信息
	 * @param userId
	 * @return
	 */
	public User getUserByUserId(String userId); 
	
	
	/**
	 * 获取所有菜单及功能菜单
	 * @return
	 */
	public QueryResult<Privilege> getAllPrivileges();
	 
}
