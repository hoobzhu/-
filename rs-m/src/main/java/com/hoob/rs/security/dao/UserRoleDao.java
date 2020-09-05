package com.hoob.rs.security.dao;



import java.util.HashMap;

import com.hoob.rs.comm.dao.BaseDao;

/**
 * @author mayjors
 * 2017年9月8日
 */
public interface UserRoleDao extends BaseDao {

    public void deleteUserRole(HashMap<String, Object> conditions);
}
