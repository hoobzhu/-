/**
 * 
 */
package com.hoob.rs.sys.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hoob.rs.comm.dao.BaseDaoImpl;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.sys.model.SysConfig;

/**
 * @author Raul	
 * 2017年9月6日
 */
@Repository("sysDao")
public class SysDaoImpl extends BaseDaoImpl implements SysDao {

	@Override
	public List<SysConfig> listAll() {		
		HashMap<String, String> orderBy = new HashMap<String, String>();
		orderBy.put("id", "asc");
		QueryResult<SysConfig> queryResult = query(SysConfig.class, -1, -1, orderBy);
		if(null!=queryResult) {
            return queryResult.getResults();
        } else {
            return null;
        }
	}

	
}
