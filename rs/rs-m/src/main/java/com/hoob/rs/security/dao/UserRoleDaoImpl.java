package com.hoob.rs.security.dao;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;

import java.util.HashMap;

/**
 * @author mayjors
 * 2017年9月8日
 */
@Repository(value="userRoleDao")
public class UserRoleDaoImpl extends BaseDaoImpl implements UserRoleDao {
	@Override
	public void deleteUserRole(HashMap<String, Object> conditions) {
		StringBuilder jpql = new StringBuilder("delete from user_role where 1=1 ");
		if (conditions.get("role_id") != null) {
            jpql.append(" and role_id = :role_id ");
        }
		if (conditions.get("user_id") != null) {
            jpql.append(" and user_id = :user_id ");
        }

		super.execNativeUpdate(jpql, conditions);
	}
}
