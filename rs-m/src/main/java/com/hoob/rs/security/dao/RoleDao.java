package com.hoob.rs.security.dao;


import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.vo.RoleVO;

import java.util.HashMap;

/**
 * @author mayjors
 * 2017年8月31日
 */
public interface RoleDao extends BaseDao{

    public QueryResult<RoleVO> queryResult(HashMap<String, Object> conditions, int firstResult, int maxResult);

	public Role getRoleByName(String name);
}
