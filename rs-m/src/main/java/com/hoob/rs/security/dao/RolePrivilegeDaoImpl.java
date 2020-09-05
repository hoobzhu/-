package com.hoob.rs.security.dao;


import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;

import java.util.HashMap;

/**
 * @author mayjors
 * 2017年9月8日
 */
@Repository(value="rolePrivilegeDao")
public class RolePrivilegeDaoImpl extends BaseDaoImpl implements RolePrivilegeDao {
    @Override
    public void deleteRolePrivilege(HashMap<String, Object> conditions) {
        StringBuffer jpql = new StringBuffer("delete from role_privilege where 1=1 ");
        if(conditions.get("role_id")!=null) {
            jpql.append(" and role_id = :role_id ");
        }
        if(conditions.get("priv_id")!=null) {
            jpql.append(" and priv_id = :priv_id ");
        }

        super.execNativeUpdate(jpql, conditions);
    }
}
