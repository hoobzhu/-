package com.hoob.rs.security.dao;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.vo.RoleVO;


/**
 * @author mayjors
 * 2017年8月31日
 */
@Repository(value="roleDao")
public class RoleDaoImpl extends BaseDaoImpl implements RoleDao {


    @Override
    public QueryResult<RoleVO> queryResult(HashMap<String, Object> conditions, int firstResult, int maxResult) {
        StringBuffer jpql = new StringBuffer("select new com.hoob.rs.security.vo.RoleVO(r.id,r.name,r.description,r.creator,r.createTime,r.updateTime) ");
//        StringBuffer jpql = new StringBuffer("");
        jpql.append(" from Role r where 1=1 ");
        if(conditions.get("name")!=null) {
            jpql.append(" and r.name like :name");
        }
        if(conditions.get("creator")!=null) {
            jpql.append(" and r.creator = :creator");
        }
        jpql.append(" order by r.id desc ");
        //QueryResult<RoleVO> q = query(jpql, conditions, firstResult, maxResult);

        return query(jpql, conditions, firstResult, maxResult);
    }

	@Override
	public Role getRoleByName(String name) {
		String jpql = "FROM Role r WHERE r.name = :name";
		HashMap<String, Object> params = new HashMap<>();
		params.put("name", name);
		Role r = findOne(jpql, params);
		return r;
	}
}
