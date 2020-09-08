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
import com.hoob.rs.security.dao.UserDao;
import com.hoob.rs.security.dao.UserRoleDao;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.RoleVO;
import com.hoob.rs.security.vo.UserVO;
import com.hoob.rs.utils.AESUtil;
import com.hoob.rs.utils.PasswordEncode;
import com.hoob.rs.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.security.dao.UserDao;
import com.hoob.rs.security.dao.UserRoleDao;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.RoleVO;
import com.hoob.rs.security.vo.UserVO;
import com.hoob.rs.utils.AESUtil;
import com.hoob.rs.utils.PasswordEncode;
import com.hoob.rs.utils.StringUtils;


/**
 * @author mayjors	
 * 2017年8月31日
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl implements UserService{
	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
    UserDao userDao;
	@Resource
    UserRoleDao userRoleDao;
	@Resource
	private UserTokenService userTokenService;
	@Override
	public void addUser(UserVO userVO) {
		if(userVO != null) {
			User user = new User();
			BeanUtils.copyProperties(userVO, user);
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			try {
				user.setPassword(AESUtil.encrypt(encodePw("111111")));
			} catch (Exception e) {
				log.error("AESUtil encrypt password error");
				e.printStackTrace();
			}
			Set<Role> roles = new HashSet<Role>();
			if (userVO.getRoles() != null && userVO.getRoles().size()>0) {
				for (RoleVO roleVO : userVO.getRoles()) {
					Role role = new Role();
					BeanUtils.copyProperties(roleVO, role);
					roles.add(role);
				}
			}
			user.setRoles(roles);
			super.addBaseEntity(user);
		}
	}
	
	@Override
	public void addUser(User user) {
		if(user != null) {
//			User user = new User();
//			BeanUtils.copyProperties(userVO, user);
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setPassword(encodePw("111111"));
//			Set<Role> roles = new HashSet<Role>();
//			if (userVO.getRoles().size()>0) {
//				for (RoleVO roleVO : userVO.getRoles()) {
//					Role role = new Role();
//					BeanUtils.copyProperties(roleVO, role);
//					roles.add(role);
//				}
//			}
//			user.setRoles(roles);
			super.addBaseEntity(user);
		}
	}
	/**
	 * 用户登录密码进行两次md5加密
	 * @param oPassword
	 * @return
	 */
	public String encodePw(String oPassword) {
		return PasswordEncode.md5Password(PasswordEncode.md5Password(oPassword));
	}

	@Override
	public void updateUser(User user) {
		if(user != null) {
			user.setUpdateTime(new Date());
			super.updateBaseEntity(user);
		}
	}
	@Override
	public void updateUserLoginInfo(User user) {
		if(user != null) {
			user.setLastLoginTime(new Date());
    		user.setLoginTimes(user.getLoginTimes()!=null?user.getLoginTimes()+1:1);
			super.updateBaseEntity(user);
		}
	}

    @Override
    public void updateUserVO(UserVO userVO) {
        if(userVO != null) {

            userVO.setUpdateTime(new Date());
            User user = new User();
            BeanUtils.copyProperties(userVO, user);
            if (userVO.getPwFlag()) {
				user.setPassword(encodePw("111111"));
			}
            Set<Role> roles = new HashSet<Role>();
            if (userVO.getRoles() != null) {
                for (RoleVO roleVO : userVO.getRoles()) {
                    Role role = new Role();
                    BeanUtils.copyProperties(roleVO, role);
                    roles.add(role);
                }
            }
            user.setRoles(roles);
         /*   if("cp".equalsIgnoreCase(user.getRole())){
            	user.setCps(null);
                user.setSpId(null);
            }else if("sp".equalsIgnoreCase(user.getRole())){
            	user.setCpId(null);
            	//sp用户关联内容提供商
            	Set<ContentProvider> cps = new HashSet<ContentProvider>();
                if (userVO.getCps() != null) {
                    for (ContentProviderVO cpVO : userVO.getCps()) {
                    	ContentProvider cp = new ContentProvider();
                        BeanUtils.copyProperties(cpVO, cp);
                        cps.add(cp);
                    }
                }
                user.setCps(cps);
            }*/
            super.updateBaseEntity(user);
        }
    }
    /****/
    @Override
	public void removeUser(Class<? extends BaseEntity> user, Serializable... entityIds) {
		if (entityIds != null) {
			for (Serializable id : entityIds) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("user_id", id);
				userRoleDao.deleteUserRole(params);
			}
			userDao.remove(user, entityIds);
		}
	}

	@Override
	public UserVO getUserVO(Serializable entityId) {
		if (entityId != null) {
			User user = getUser(entityId);
			UserVO userVO = new UserVO();
			if (user != null) {
				List<RoleVO> roles = new ArrayList<RoleVO>();
			//	List<ContentProviderVO> cps = new ArrayList<ContentProviderVO>();
				BeanUtils.copyProperties(user, userVO);
				for (Role role : user.getRoles()) {
					RoleVO roleVO = new RoleVO();
					BeanUtils.copyProperties(role, roleVO);
					roles.add(roleVO);
				}
				userVO.setRoles(roles);
			/*	for (ContentProvider cp : user.getCps()) {
					ContentProviderVO cpVO = new ContentProviderVO();
					BeanUtils.copyProperties(cp, cpVO);
					cps.add(cpVO);
				}
				userVO.setCps(cps);*/
				return userVO;
			}
		}
		return null;
	}

	@Override
	public User getUser(Serializable entityId) {
		if (entityId != null) {
			User user = userDao.find(User.class, entityId);
			return user;
		}
		return null;
	}

	@Override
	public User getUserByUserId(String userId) {
		if (userId != null) {
			User user = userDao.findUserByUserId(userId);
			return user;
		}
		return null;
	}

	@Override
	public UserVO getUserVOByUserId(String userId) {
		if (userId != null) {
			User user = getUserByUserId(userId);
			UserVO userVO = new UserVO();
			if (user != null) {
				BeanUtils.copyProperties(user, userVO);
				return userVO;
			}
		}
		return null;
	}

	@Override
	public QueryResult<UserVO> getQuery(String userId, String cpid, String spid,
                                        String enable, String parentUserId, Integer firstResult, Integer maxResult) {
		HashMap<String, Object> params = new HashMap<String, Object>();
        if (userId != null) {
            params.put("userId", StringUtils.likeStr(userId));
        }
        if (cpid != null) {
            params.put("cpId", StringUtils.likeStr(cpid));
        }
        if (spid != null) {
            params.put("spId", StringUtils.likeStr(spid));
        }
        if (enable != null) {
            params.put("enable", "true".equals(enable) ? true: false);
        }
        if (parentUserId != null && !"sysadmin".equals(parentUserId)) {
            params.put("parentUserId", parentUserId);
        }
        
        QueryResult<User> q = userDao.queryResult(params, firstResult, maxResult);
		QueryResult<UserVO> queryResult = new QueryResult<UserVO>();
		if (q != null && q.getCount()>0) {
			List<User> list = q.getResults();
			List<UserVO> userVOList = new ArrayList<UserVO>();
			for (User user: list) {
				UserVO userVO = new UserVO();
				BeanUtils.copyProperties(user, userVO);
				if (user.getRoles().size()>0) {
					String roleName = null;
					for (Role role : user.getRoles()) {
						roleName = roleName == null ? role.getName() : (roleName + "," +role.getName());
					}
					userVO.setRoleName(roleName);
				}
				userVOList.add(userVO);
			}
			queryResult.setCount(q.getCount());
			queryResult.setResults(userVOList);
		}
		return queryResult;
	}

	@Override
	public boolean enableUser(User user) {
		boolean enable = true;
		try {
			user.setEnable(!user.getEnable());
			user.setUpdateTime(new Date());
			userDao.merge(user);
			enable = user.getEnable();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return enable;
	}

	@Override
	public void resetPassword(User user) throws Exception {
			user.setPassword(AESUtil.encrypt(encodePw("111111")));
			user.setUpdateTime(new Date());
			userDao.merge(user);
	}

	@Override
	public QueryResult<User> getUsers(String userId, String cpid, String spid, String enable, Integer firstResult,
			Integer maxResult) {
		HashMap<String, Object> params = new HashMap<String, Object>();
        if (userId != null) {
            params.put("userId", StringUtils.likeStr(userId));
        }
        if (cpid != null) {
            params.put("cpId", StringUtils.likeStr(cpid));
        }
        if (spid != null) {
            params.put("spId", StringUtils.likeStr(spid));
        }
        if (enable != null) {
            params.put("enable", "true".equals(enable) ? true: false);
        }

        QueryResult<User> queryResult = userDao.queryResult(params, firstResult, maxResult);
//		QueryResult<UserVO> queryResult = new QueryResult<UserVO>();
//		if (q != null && q.getCount()>0) {
//			List<User> list = q.getResults();
//			List<UserVO> userVOList = new ArrayList<UserVO>();
//			for (User user: list) {
//				UserVO userVO = new UserVO();
//				BeanUtils.copyProperties(user, userVO);
//				if (user.getRoles().size()>0) {
//					String roleName = null;
//					for (Role role : user.getRoles()) {
//						roleName = roleName == null ? role.getName() : (roleName + "," +role.getName());
//					}
//					userVO.setRoleName(roleName);
//				}
//				userVOList.add(userVO);
//			}
//			queryResult.setCount(q.getCount());
//			queryResult.setResults(userVOList);
//		}
		return queryResult;
	}

}
