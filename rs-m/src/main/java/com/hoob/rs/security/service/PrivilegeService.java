package com.hoob.rs.security.service;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.PrivilegeVO;

import java.io.Serializable;
import java.util.List;

/**
 * @author mayjors
 * 2017年9月8日
 */
public interface PrivilegeService extends BaseService {

    public void addPrivilege(PrivilegeVO privilegeVO);

    public void updatePrivilege(PrivilegeVO privilegeVO);

    public void removePrivilege(Class<? extends BaseEntity> privilege, Serializable... entityIds);

    public PrivilegeVO getPrivilegeVo(Serializable entityId);

    public Privilege getPrivilege(Serializable entityId);

    public Privilege getPrivilegeByCode(Serializable entityId);

    public QueryResult<PrivilegeVO> getQuery(String menuCode, String menuLevel, Integer parent, String enable, Integer firstResult, Integer maxResult);

    /**
     * 获取用户所具有的权限
     * @param user
     * @return
     */
    public List<PrivilegeVO> getPrivileges(User user);
}
