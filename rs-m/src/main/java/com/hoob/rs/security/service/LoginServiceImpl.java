package com.hoob.rs.security.service;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.security.dao.LoginDao;
import com.hoob.rs.security.model.Privilege;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.UserSession;
import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.utils.AESUtil;


/**
 * 登录服务接口实现类
 * @author Raul	
 * 2017年9月1日
 */
@Service(value="loginService")
@Transactional
public class LoginServiceImpl implements LoginService{

	@Resource
	LoginDao loginDao;
	
	@Resource
	private SysConfigService sysConfigService;
	
	@Override
	public UserSession authorize(String userId, String password) throws Exception {
		
		if(null==userId) {
            return null;
        }
		User user  = loginDao.getUserByUserId(userId);	
		if(null==user) {
            return null;
        }
		
		//测试模式不需要验证密码
		String testMode = sysConfigService.find(com.hoob.rs.sys.constant.ConfigKey.TEST_MODE).getValue();
		if (!"1".equals(testMode)) {
			if(!password.equals(AESUtil.decrypt(user.getPassword()))){
				return null;
			}
			
		}
		
		//构建会话
		UserSession session = new UserSession();
		Date loginTime = new Date();		
		session.setLastActiveTime(loginTime);
		session.setPassword(password);
		session.setUserId(userId);
		
		QueryResult<Privilege> allPrivileges = loginDao.getAllPrivileges();
		
		if(User.Type.SYSADMIN.name().equalsIgnoreCase(user.getUserType())){			
			List<Privilege> list = allPrivileges.getResults();
			if(null!=list){
				for(Privilege p : list){
					session.getIfPrivileges().add(p.getUrl());
					session.getMenuPrivileges().add(p.getMenuCode());
				}
			}			
		}else{
			Set<Role> roles = user.getRoles();	
			for(Iterator<Role> i = roles.iterator();i.hasNext();){
				Role r = i.next();
				Set<Privilege> privileges = r.getPrivileges();
				for(Iterator<Privilege> j = privileges.iterator();j.hasNext();){
					Privilege p = j.next();
					if(!session.getIfPrivileges().contains(p.getUrl())) {
                        session.getIfPrivileges().add(p.getUrl());
                    }
					if(!session.getMenuPrivileges().contains(p.getMenuCode())) {
                        session.getMenuPrivileges().add(p.getMenuCode());
                    }
					this.findPrevPrivileges(allPrivileges.getResults(), session, p);
				}
			}
		}
		
		return session;
	}
	
	/**
	 * 查找父级权限，将其加入授权中
	 * @param allPrivileges
	 * @param session
	 * @param ownerPrivilege
	 */
	private void findPrevPrivileges(List<Privilege> allPrivileges,UserSession session,Privilege ownerPrivilege){
		if(null==allPrivileges) {
            return;
        }
		if(null==session) {
            return;
        }
		if(null==ownerPrivilege) {
            return;
        }
		
		if(ownerPrivilege.getMenuLevel()==0) {
            return;
        }
		Privilege parentPrivilege = ownerPrivilege.getParent();
		if(null!=parentPrivilege){
			String menuCode = parentPrivilege.getMenuCode();
			String ifc = parentPrivilege.getUrl();
			if(!session.getMenuPrivileges().contains(menuCode)) {
                session.getMenuPrivileges().add(menuCode);
            }
			if(!session.getIfPrivileges().contains(ifc)) {
                session.getIfPrivileges().add(ifc);
            }
			findPrevPrivileges(allPrivileges,session,parentPrivilege);
		}
	}
	

}
