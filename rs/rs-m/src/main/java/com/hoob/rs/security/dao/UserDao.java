package com.hoob.rs.security.dao;



import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.UserVO;
import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;

import java.util.HashMap;

/**
 * @author ziven
 * 2017年8月31日
 */
public interface UserDao extends BaseDao {

	public User findUserByUserId(String userId);

	public UserVO findUserVOByUserId(String userId);

	public QueryResult<User> queryResult(HashMap<String, Object> conditions, int firstResult, int maxResult);
	
}
