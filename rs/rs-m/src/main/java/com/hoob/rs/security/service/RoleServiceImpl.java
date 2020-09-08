package com.hoob.rs.security.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.security.dao.PrivilegeDao;
import com.hoob.rs.security.dao.RoleDao;
import com.hoob.rs.security.dao.RolePrivilegeDao;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.vo.RoleVO;
import com.hoob.rs.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.security.dao.PrivilegeDao;
import com.hoob.rs.security.dao.RoleDao;
import com.hoob.rs.security.dao.RolePrivilegeDao;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.vo.RoleVO;
import com.hoob.rs.utils.StringUtils;

/**
 * @author mayjors	
 * 2017年8月31日
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseServiceImpl implements RoleService{
	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
	private RoleDao roleDao;
	@Resource
	private RolePrivilegeDao rolePrivilegeDao;
	@Resource
	private PrivilegeDao privilegeDao;
	
	@Override
	public void addRole(RoleVO roleVO) {
		if(roleVO != null) {
			Set<Privilege> privileges = new HashSet<Privilege>();
			if (roleVO.getPriId()!= null && roleVO.getPriId().size()>0) {
				for (Long id : roleVO.getPriId()) {
					Privilege privilege = privilegeDao.find(Privilege.class, id);
					privileges.add(privilege);
				}
			}
			Role role = new Role();
			BeanUtils.copyProperties(roleVO, role);
			role.setCreateTime(new Date());
			role.setUpdateTime(new Date());
			role.setPrivileges(privileges);

			super.addBaseEntity(role);
		}
	}

	@Override
	public void updateRole(RoleVO roleVO) {
		if(roleVO != null) {
			Set<Privilege> privileges = new HashSet<Privilege>();
			if (roleVO.getPriId()!=null && roleVO.getPriId().size()>0) {
				for (Long id : roleVO.getPriId()) {
					Privilege privilege = privilegeDao.find(Privilege.class, id);
					privileges.add(privilege);
				}
			}
			Role role = new Role();
			BeanUtils.copyProperties(roleVO, role);
			role.setUpdateTime(new Date());
			role.setPrivileges(privileges);

			super.updateBaseEntity(role);
		}
	}

	@Override
	public void removeRole(Class<? extends BaseEntity> role, Serializable... entityIds) {
		if (entityIds != null) {
			for (Serializable id : entityIds) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("role_id", id);
				rolePrivilegeDao.deleteRolePrivilege(params);
			}
			roleDao.remove(role, entityIds);
		}
	}

	@Override
	public RoleVO getRoleVo(Serializable entityId) {
		if (entityId != null) {
			Role role = getRole(entityId);
			RoleVO roleVO = new RoleVO();
			if (role != null) {
				BeanUtils.copyProperties(role, roleVO);
				List<Long> priIds = new ArrayList<Long>();
				for (Privilege privilege : role.getPrivileges()) {
                    priIds.add(privilege.getId());
				}
				roleVO.setPriId(priIds);
				return roleVO;
			}
		}
		return null;
	}

	@Override
	public Role getRole(Serializable entityId) {
		if (entityId != null) {
			Role role = roleDao.find(Role.class, entityId);
			return role;
		}
		return null;
	}

	@Override
	public QueryResult<RoleVO> getQuery(String name, String creator, Integer firstResult, Integer maxResult) {
		HashMap<String, Object> params = new HashMap<String, Object>();
        if (name != null) {
            params.put("name", StringUtils.likeStr(name));
        }
        if (creator != null && !"sysadmin".equals(creator)) {
            params.put("creator", creator);
        }
        
        QueryResult<RoleVO> rs = roleDao.queryResult(params, firstResult, maxResult);
		return rs;
	}
	
	/**
	 * 判断role name是否可以使用
	 * @param name
	 * @return 如果可用则返回true，不可用返回false
	 */
	@Override
	public boolean validRoleName(String name){
		
		Role r = roleDao.getRoleByName(name);
		if(r == null){
			return true;
		}
		return false;
	}

}
