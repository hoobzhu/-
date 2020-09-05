package com.hoob.rs.security.dao;



import java.util.HashMap;

import com.hoob.rs.comm.dao.BaseDao;

/**
 * @author mayjors
 * 2017年9月8日
 */
public interface RolePrivilegeDao extends BaseDao {

    public void deleteRolePrivilege(HashMap<String, Object> conditions);
}
