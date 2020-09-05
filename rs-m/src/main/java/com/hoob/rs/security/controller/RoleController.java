package com.hoob.rs.security.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.vo.ListResponse;
import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.comm.vo.VoResponse;
import com.hoob.rs.security.model.Role;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.service.RoleService;
import com.hoob.rs.security.service.UserTokenService;
import com.hoob.rs.security.vo.RoleVO;
import com.hoob.rs.utils.SessionManager;
import com.hoob.rs.utils.StringUtils;

/**
 * @author mayjors
 * 2017年8月31日
 */

@RestController
@RequestMapping("/")
public class RoleController {
	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	
	@Resource
	private RoleService roleService;
	@Resource
	private UserTokenService userTokenService;
	
	@RequestMapping(method=RequestMethod.POST,path="/v1/security/role/add", consumes={"application/json"},produces={"application/json"})
    public Response add(@RequestBody RoleVO roleVO, HttpServletRequest request){
		log.debug("add role ");
		Response response = new Response();
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				String creator = SessionManager.getCurrentUser(token).getUserId();
				//String[] parts = token.split(":");
				//String creator = parts[0];
				if(roleVO.getName() == null || "".equals(roleVO.getName())) {
					response.setResultCode(101);
					response.setDescription("Save failed! Role name is empty.");
				} else if(!roleService.validRoleName(roleVO.getName())){
					response.setResultCode(102);
					response.setDescription("Save failed! This role name already exists!");
				}
				else{	
					roleVO.setCreator(creator);
					roleService.addRole(roleVO);
				}
				
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
			response.setDescription("Fail");
		}
    	return response;
    }
	
	@RequestMapping(method=RequestMethod.POST,path="/v1/security/role/edit", consumes={"application/json"},produces={"application/json"})
    public Response update(@RequestBody RoleVO roleVO){
		log.debug("update role ");
		Response response = new Response();
		try {
			if(roleVO.getName() == null) {
				response.setResultCode(-1);
				response.setDescription("cpId is null");
			} else {
				roleService.updateRole(roleVO);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
			response.setDescription("Fail");
		}
    	return response;
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/v1/security/role/detail")
    public VoResponse<RoleVO> get(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("get role ");
		VoResponse<RoleVO> response = new VoResponse<RoleVO>(0);
		RoleVO roleVO = null;
        try {
        	if(id != null) {
				long idLong = StringUtils.handleLongParam(id);
				roleVO = roleService.getRoleVo(idLong);
        	}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		response.setVo(roleVO);
        return response;
    }
	
	@RequestMapping(method=RequestMethod.GET,path="/v1/security/role/list")
    public ListResponse<RoleVO> getList(@RequestParam(value = "name",required=false) String name,
								@RequestParam(value = "first",required=false) String first,
								@RequestParam(value = "max",required=false) String max,
    		HttpServletRequest request){
		log.debug("get role list ");
		ListResponse<RoleVO> response = new ListResponse<RoleVO>(0);
		name = StringUtils.handleStrParam(name);
		Integer firstInt = 0;
		Integer maxInt = -1;
		firstInt = StringUtils.handleIntParam(first)==null ? 0 : StringUtils.handleIntParam(first);
		maxInt = StringUtils.handleIntParam(max)== null ? -1 : StringUtils.handleIntParam(max);

		//long total = 0;
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				User currentUser = userTokenService.getCurrentUser(token);
				QueryResult<RoleVO> queryResult = roleService.getQuery(name,currentUser.getUserId(),firstInt, maxInt);
				if(queryResult != null) {
					response.setList(queryResult.getResults());
					response.setTotal(queryResult.getCount());
				}
			}else{
				log.error("user token is null !");
				response = new ListResponse<RoleVO>(-1);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new ListResponse<RoleVO>(-1);
		}
        return response;
    }
	
	@RequestMapping(method=RequestMethod.POST,path="/v1/security/role/remove", consumes={"application/json"},produces={"application/json"})
    public Response delete(@RequestBody List<Long> ids){
		log.debug("delete");
		Response response = new Response(0);
		try {
//			if (ids.size()> 0) {
//				Long longArray[]=new Long[ids.size()];
//				for(int i=0,j=ids.size();i<j;i++){
//					longArray[i]=ids.get(i);
//				}
//				roleService.removeRole(Role.class, longArray);
//			}
			for(Long id : ids){
				roleService.removeRole(Role.class, id);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(-1);
		}
    	return response;
    }

}
