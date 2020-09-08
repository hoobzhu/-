package com.hoob.rs.security.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import com.hoob.rs.security.dao.RolePrivilegeDao;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.PrivilegeVO;
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
import com.hoob.rs.security.dao.RolePrivilegeDao;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.PrivilegeVO;
import com.hoob.rs.utils.StringUtils;



/**
 * @author mayjors 2017年9月8日
 */
@Service("privilegeService")
@Transactional
public class PrivilegeServiceImpl extends BaseServiceImpl implements PrivilegeService {
	static Logger LOG = LogManager.getLogger(PrivilegeServiceImpl.class);
	@Resource
	private PrivilegeDao privilegeDao;
	@Resource
	private RolePrivilegeDao rolePrivilegeDao;

	@Override
	public void addPrivilege(PrivilegeVO privilegeVO) {
		if (privilegeVO != null) {
			Privilege privilege = new Privilege();
			BeanUtils.copyProperties(privilegeVO, privilege);
			privilege.setCreateTime(new Date());
			privilege.setUpdateTime(new Date());

			super.addBaseEntity(privilege);
		}
	}

	@Override
	public void updatePrivilege(PrivilegeVO privilegeVO) {
		if (privilegeVO != null) {
			Privilege privilege = new Privilege();
			BeanUtils.copyProperties(privilegeVO, privilege);
			privilege.setUpdateTime(new Date());

			super.updateBaseEntity(privilege);
		}
	}

	@Override
	public void removePrivilege(Class<? extends BaseEntity> privilege, Serializable... entityIds) {
		if (entityIds != null) {
			for (Serializable id : entityIds) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("priv_id", id);
				rolePrivilegeDao.deleteRolePrivilege(params);
			}
			privilegeDao.remove(privilege, entityIds);
		}
	}

	@Override
	public PrivilegeVO getPrivilegeVo(Serializable entityId) {
		if (entityId != null) {
			Privilege privilege = getPrivilege(entityId);
			PrivilegeVO privilegeVO = new PrivilegeVO();
			if (privilege != null) {
				BeanUtils.copyProperties(privilege, privilegeVO);
				PrivilegeVO parentPriv = new PrivilegeVO();
				if (privilege.getId() == 1) {
					privilegeVO.setParent(null);
				} else {
					BeanUtils.copyProperties(privilege.getParent(), parentPriv);
					privilegeVO.setParentId(parentPriv.getId());
					privilegeVO.setParent(parentPriv);
				}
				return privilegeVO;
			}
		}
		return null;
	}

	@Override
	public Privilege getPrivilege(Serializable entityId) {
		if (entityId != null) {
			Privilege privilege = privilegeDao.find(Privilege.class, entityId);
			return privilege;
		}
		return null;
	}

	@Override
	public Privilege getPrivilegeByCode(Serializable entityId) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (entityId != null) {
            params.put("menuCode", entityId);
        }

		Privilege privilege = privilegeDao.get("from Privilege p where p.menuCode = :menuCode ", params);
		return privilege;
	}

	@Override
	public QueryResult<PrivilegeVO> getQuery(String menuCode, String menuLevel, Integer parent, String enable,
                                             Integer firstResult, Integer maxResult) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (menuCode != null) {
            params.put("menuCode", StringUtils.likeStr(menuCode));
        }
		if (menuLevel != null) {
            params.put("menuLevel", StringUtils.likeStr(menuLevel));
        }
		if (parent != null) {
            params.put("parent", parent);
        }
		if (enable != null) {
            params.put("enable", "true".equals(enable) ? true : false);
        }

		QueryResult<Privilege> q = privilegeDao.queryResult(params, firstResult, maxResult);
		QueryResult<PrivilegeVO> queryResult = new QueryResult<PrivilegeVO>();
		if (q != null && q.getCount() > 0) {
			List<Privilege> list = q.getResults();
			List<PrivilegeVO> privilegeVOList = new ArrayList<PrivilegeVO>();
			for (Privilege privilege : list) {
				// System.out.println(privilege);
				PrivilegeVO privilegeVO = new PrivilegeVO();
				BeanUtils.copyProperties(privilege, privilegeVO);
				if (privilege.getParent() != null) {
					privilegeVO.setParentId(privilege.getParent().getId());
					PrivilegeVO parentPriv = new PrivilegeVO();
					BeanUtils.copyProperties(privilege.getParent(), parentPriv);
					privilegeVO.setParent(parentPriv);
				} else {
					privilegeVO.setParentId(0);
				}
				privilegeVOList.add(privilegeVO);
			}
			queryResult.setCount(q.getCount());
			queryResult.setResults(privilegeVOList);
		}

		return queryResult;
	}

	@Override
	public List<PrivilegeVO> getPrivileges(User user) {
		List<PrivilegeVO> pList = new ArrayList<PrivilegeVO>();
		Set<Role> roles = new HashSet<Role>();
		try {
			roles = user.getRoles();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		if ("sysadmin".equals(user.getUserId())) {
			// 获取所有的权限
			QueryResult<PrivilegeVO> queryResult = getQuery(null, null, null, null, 0, -1);
			if (queryResult != null) {
				pList = queryResult.getResults();
			}
		} else {
			PrivilegeVO firstP = getPrivilegeVo((long) 1);
			pList = new ArrayList<PrivilegeVO>();
			Set<Long> uniqueKeySet = new HashSet<Long>();
			if (null != firstP) {
				pList.add(firstP);
				uniqueKeySet.add(firstP.getId());
			}
			for (Role r : roles) {
				for (Privilege p : r.getPrivileges()) {
					PrivilegeVO privilegeVO = new PrivilegeVO();
					BeanUtils.copyProperties(p, privilegeVO);
					if (p.getParent() != null) {
						privilegeVO.setParentId(p.getParent().getId());
					} else {
						privilegeVO.setParentId(0);
					}
					if (p.getMenuLevel() >= 2 && p.getParent() != null
							&& !uniqueKeySet.contains(p.getParent().getId())) {
						PrivilegeVO parentPV = new PrivilegeVO();
						BeanUtils.copyProperties(p.getParent(), parentPV);
						// parentPV.setParent(p.getParent());
						if (p.getParent().getParent() != null) {
							parentPV.setParentId(p.getParent().getParent().getId());
						}
						pList.add(parentPV);
						uniqueKeySet.add(parentPV.getId());
					}
					if(!uniqueKeySet.contains(privilegeVO.getId())){
						pList.add(privilegeVO);
						uniqueKeySet.add(privilegeVO.getId());
					}
				}
			}
		}
		//根据id排序
		Collections.sort(pList);
		return pList;
	}
}
