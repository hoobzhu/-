package com.hoob.rs.security.service;

import java.io.Serializable;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.vo.RoleVO;
import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.vo.RoleVO;

/**
 * @author mayjors
 * 2017年8月31日
 */
public interface RoleService extends BaseService {
	
	public void addRole(RoleVO roleVO);
	
	public void updateRole(RoleVO roleVO);
	
	public void removeRole(Class<? extends BaseEntity> role, Serializable... entityIds);
	
	public RoleVO getRoleVo(Serializable entityId);

	public Role getRole(Serializable entityId);

	public QueryResult<RoleVO> getQuery(String name, String creator, Integer firstResult, Integer maxResult);
	
	/**
	 * 判断role name是否可以使用
	 * @param name
	 * @return 如果可用则返回true，不可用返回false
	 */
	public boolean validRoleName(String name);

}
