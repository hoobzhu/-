package com.hoob.rs.security.dao;



import java.util.HashMap;

import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;

/**
 * @author mayjors
 * 2017年9月8日
 */
public interface PrivilegeDao extends BaseDao {

    public QueryResult<Privilege> queryResult(HashMap<String, Object> conditions, int firstResult, int maxResult);
}
