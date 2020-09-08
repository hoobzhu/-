package com.hoob.rs.security.dao;

import java.util.HashMap;


import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.UserVO;


/**
 * @author ziven
 * 2017年8月31日
 */
@Component("userDao")
@Transactional
public class UserDaoImpl extends BaseDaoImpl implements UserDao{

	Logger logger = LogManager.getLogger(UserDaoImpl.class);

	@Override
	public User findUserByUserId(String userId) {
		if(null==userId) {
            return null;
        }
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		try{
			return this.get("from User u where u.userId = :userId", params);
		}catch(Exception ex){
			logger.error(ex);
			return null;
		}
	}

	@Override
	public UserVO findUserVOByUserId(String userId) {
		if(null==userId) {
            return null;
        }
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		try{
			UserVO userVO = this.get("select new com.fonsview.rs.security.vo.UserVO(u.id,u.userId,"
					+ "u.cpId,u.spId ) from User u where u.userId = :userId", params);
			return userVO;
		}catch(Exception ex){
			logger.error(ex);
			return null;
		}
	}

	@Override
	public QueryResult<User> queryResult(HashMap<String, Object> conditions, int firstResult, int maxResult) {
		StringBuffer jpql = new StringBuffer("");
//		StringBuffer jpql = new StringBuffer("select new com.fonsview.cms.security.vo.UserVO(u.id,u.roles.id )");
//		StringBuffer jpql = new StringBuffer("select new com.fonsview.cms.security.vo.UserVO(u.id,u.name,u.role,u.nickName,u.lastLoginTime,u.loginTimes,u.enable, ");
//		jpql.append("u.userId,u.parentUserId,u.userlevel,u.userType,u.cpId,u.spId,u.createTime,u.updateTime) ");
		jpql.append(" from User u where 1=1 ");
		if(conditions.get("userId")!=null) {
            jpql.append(" and u.userId like :userId");
        }
		if(conditions.get("cpId")!=null) {
            jpql.append(" and u.cpId like :cpId");
        }
		if(conditions.get("spId")!=null) {
            jpql.append(" and u.spId like :spId");
        }
		if(conditions.get("enable")!=null) {
            jpql.append(" and u.enable like :enable");
        }
		if(conditions.get("parentUserId")!=null) {
            jpql.append(" and u.parentUserId = :parentUserId");
        }
		jpql.append(" order by u.id desc ");

//		StringBuffer stringBuffer = new StringBuffer("select u.roles from User u left join u.roles r where 1=1 ");
//		StringBuffer stringBuffer = new StringBuffer("select new com.fonsview.cms.security.vo.UserVO(u.roles) from User u,Role r where 1=1 and r.id in elements(u.roles) ");
//		StringBuffer stringBuffer = new StringBuffer("select new com.fonsview.cms.security.vo.UserVO(u.roles) from User u left join u.roles r where 1=1 ");
//		if(conditions.get("name")!=null) stringBuffer.append(" and u.name like :name");
//		if(conditions.get("cpId")!=null) stringBuffer.append(" and u.cpId like :cpId");
//		if(conditions.get("spId")!=null) stringBuffer.append(" and u.spId like :spId");
//		if(conditions.get("enable")!=null) stringBuffer.append(" and u.enable like :enable");
//
//		QueryResult<UserVO> query = query(stringBuffer, conditions, firstResult, maxResult);
//		QueryResult<Object> query = query(stringBuffer, conditions, firstResult, maxResult);

		QueryResult<User> queryResult = query(jpql, conditions, firstResult, maxResult);

		return queryResult;
//		return query(jpql, conditions, firstResult, maxResult);
	}


	
}
