/**
 * 
 */
package com.hoob.rs.sys.dao;

import java.util.List;

import com.hoob.rs.comm.dao.BaseDao;
import com.hoob.rs.sys.model.SysConfig;

/**
 * @author Raul	
 * 2017年9月6日
 */

public interface SysDao extends BaseDao{

	public List<SysConfig> listAll();
}
