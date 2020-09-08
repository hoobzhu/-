package com.hoob.rs.security.dao;
import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.model.Privilege;

import java.util.HashMap;

/**
 * @author mayjors
 * 2017年9月8日
 */
@Repository(value="privilegeDao")
public class PrivilegeDaoImpl extends BaseDaoImpl implements PrivilegeDao {

    @Override
    public QueryResult<Privilege> queryResult(HashMap<String, Object> conditions, int firstResult, int maxResult) {
        StringBuffer jpql = new StringBuffer("");
//        jpql.append("select new com.fonsview.cms.security.vo.PrivilegeVO(p.id,p.menuCode,p.menuTitle,p.menuLevel,p.menuType, ");
//        jpql.append("p.description, p.url,p.enable, p.parent) ");
//        jpql.append("p.description, p.url,p.enable,p.createTime,p.updateTime) ");
        jpql.append(" from Privilege p where 1=1 ");
        if(conditions.get("menuCode")!=null) {
            jpql.append(" and p.menuCode like :menuCode");
        }
        if(conditions.get("menuLevel")!=null) {
            jpql.append(" and p.menuLevel like :menuLevel");
        }
        if(conditions.get("parent")!=null) {
            jpql.append(" and p.parent like :parent");
        }
        if(conditions.get("enable")!=null) {
            jpql.append(" and p.enable = :enable");
        }

        jpql.append(" order by p.id desc ");
        QueryResult<Privilege> q = query(jpql, conditions, firstResult, maxResult);

        return q;
    }
}
