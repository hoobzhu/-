package com.hoob.rs.security.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.hoob.rs.comm.service.BaseServiceImpl;
import com.hoob.rs.security.dao.UserDao;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.UserVO;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @author mayjors
 * 2017年9月14日
 */
@Service("userTokenService")
@Transactional
public class UserTokenServiceImpl extends BaseServiceImpl implements UserTokenService {

    @Resource
    private UserDao userDao;
  

//    @Override
//    public User getCurrentUser() {
//        String token = request.getHeader("Token");
//        if (token != null) {
//            String userId = SessionManager.getCurrentUser(token).getUserId();
//            User user = userDao.findUserByUserId(userId);
//            return user;
//        }
//        return null;
//    }

    @Override
    public User getCurrentUser(String userToken) {
        if (userToken != null) {

            String[] params = userToken.split("\\:");

            String userId = params[0]; //接口请求用户

            User user  = userDao.findUserByUserId(userId);
            return user;

        }
        return null;
    }

    @Override
    public UserVO getCurrentUserVo(String userToken) {
        if (userToken != null) {

            String[] params = userToken.split("\\:");

            String userId = params[0]; //接口请求用户

            UserVO userVO = userDao.findUserVOByUserId(userId);

            return userVO;

        }
        return null;
    }

/*    @Override
    public ContentProvider getCurrentCP(String userToken) {
        User user = getCurrentUser(userToken);
        String cpId = user.getCpId();
        if (cpId != null) {
            ContentProvider contentProvider = cpService.getCpByCPId(cpId);

            return contentProvider;
        }
        return null;
    }*/

  /*  @Override
    public ServiceProvider getCurrentSP(String userToken) {
        User user = getCurrentUser(userToken);
        String spId = user.getSpId();
        if (spId != null) {
            ServiceProvider serviceProvider = spService.getSpBySpId(spId);

            return serviceProvider;
        }
        return null;
    }
*/
  /*  @Override
    public Set<Long> getCPIds(String userToken) {
        Set<Long> cpIds = new HashSet<Long>();
        if (userToken != null) {
            User user = getCurrentUser(userToken);
            if (user != null) {
                cpIds = getCPIdsByUser(user);

                return cpIds;
            }
        }
        return null;
    }
*/
  /*  @Override
    public Set<Long> getCPIdsByUser(User user) {
        Set<Long> cpIds = new HashSet<Long>();
        if (user != null) {
            String spId = user.getSpId();
            String cpId = user.getCpId();
            if (spId != null) {
                ServiceProvider serviceProvider = spService.getSpBySpId(spId);
                Set<ContentProvider> set = serviceProvider.getCps();
                if (set.size() > 0) {
                    for (ContentProvider contentProvider : set) {
                        cpIds.add(contentProvider.getId());
                    }
                }
            } else if (cpId != null) {
                ContentProvider contentProvider = cpService.getCpByCPId(cpId);
                cpIds.add(contentProvider.getId());
            }
            return cpIds;
        }
        return null;
    }*/
/*
    @Override
    public List<ContentProvider> getCpsByCurrUser(String userToken) {
        List<ContentProvider> contentProviderList = new ArrayList<ContentProvider>();
        UserVO user = getCurrentUserVo(userToken);
        if (user != null) {
            contentProviderList = getCpsByCurrUser(user);

            return contentProviderList;
        }
        return null;
    }
*/
/*    @Override
    public List<ContentProvider> getCpsByCurrUser(UserVO userVO) {
        List<ContentProvider> contentProviderList = new ArrayList<ContentProvider>();
        if (userVO != null) {
            String spId = userVO.getSpId();
            String cpId = userVO.getCpId();
            if (StringUtils.isNotEmpty(spId)) {
                ServiceProvider serviceProvider = spService.getSpBySpId(spId);
                if (serviceProvider != null) {

                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("user_id", userVO.getId());
                    contentProviderList = userCpDao.queryResult(params, -1, -1);
                }
            } else if (StringUtils.isNotEmpty(cpId)) {
                ContentProvider contentProvider = cpService.getCpByCPId(cpId);
                contentProviderList.add(contentProvider);
            }
            return contentProviderList;
        }
        return null;
    }*/

  /*  @Override
    public Set<Long> getLongCpIdsByCurrUser(String userToken) {
        Set<Long> cpIds = new HashSet<Long>();
        User user = getCurrentUser(userToken);
        if (user != null) {
            cpIds = getCPIdsByUser(user);

            return cpIds;
        }
        return null;
    }*/

  /*  @Override
    public Set<String> getCpIdsByCurrUser(String userToken) {
        Set<String> cpIds = new HashSet<String>();
        UserVO user = getCurrentUserVo(userToken);
        if (user != null) {
            List<ContentProvider> contentProviderList = getCpsByCurrUser(user);
            for (ContentProvider contentProvider : contentProviderList) {
                cpIds.add(contentProvider.getCpId());
            }
            return cpIds;
        }
        return null;
    }
*/
  /*  @Override
    public List<String> getCpIdsListByCurrUser(String userToken) {
        List<String> cpIds = new ArrayList<>();
        UserVO user = getCurrentUserVo(userToken);
        if (user != null) {
            List<ContentProvider> contentProviderList = getCpsByCurrUser(user);
            for (ContentProvider contentProvider : contentProviderList) {
                cpIds.add(contentProvider.getCpId());
            }
            return cpIds;
        }
        return null;
    }*/
}
