package com.hoob.rs.security.service;
import com.hoob.rs.comm.service.BaseService;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.vo.UserVO;

/**
 * @author mayjors
 * 2017年9月14日
 */
public interface UserTokenService extends BaseService {

    //public User getCurrentUser();

    public User getCurrentUser(String userToken);

    public UserVO getCurrentUserVo(String userToken);


/*
    public Set<Long> getCPIdsByUser(User user);

    public Set<Long> getLongCpIdsByCurrUser(String userToken);

    public Set<String> getCpIdsByCurrUser(String userToken);

    public List<String> getCpIdsListByCurrUser(String userToken);*/
}
