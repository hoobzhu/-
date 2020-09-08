/**
 * 
 */
package com.hoob.rs.security.dao;

import java.util.HashMap;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.User;


/**
 * @author Raul	
 * 2017年9月1日
 */
@Repository(value="loginDao")
public class LoginDaoImpl extends BaseDaoImpl implements LoginDao{
	Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Override
	public User getUserByUserId(String userId) {
		if(null==userId) {
            return null;
        }
		StringBuilder sb = new StringBuilder();
		sb.append("from User u where u.userId = :userId");
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("userId", userId);
		try{
			return this.findOne(sb.toString(), params);	
		}catch(Exception ex){
			logger.error(ex);
			return null;
		}
		
	}

	@Override
	public QueryResult<Privilege> getAllPrivileges() {
		return this.query(Privilege.class, -1, -1);
	}


}
