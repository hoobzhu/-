package com.hoob.rs.security.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hoob.rs.comm.dao.QueryResult;
import com.hoob.rs.comm.vo.ListResponse;
import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.comm.vo.VoResponse;
import com.hoob.rs.constants.StatusCode;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.service.UserService;
import com.hoob.rs.security.service.UserTokenService;
import com.hoob.rs.security.vo.UserVO;
import com.hoob.rs.utils.AESUtil;
import com.hoob.rs.utils.StringUtils;
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
import com.hoob.rs.constants.StatusCode;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.service.UserService;
import com.hoob.rs.security.service.UserTokenService;
import com.hoob.rs.security.vo.UserVO;
import com.hoob.rs.utils.AESUtil;
import com.hoob.rs.utils.PasswordEncode;
import com.hoob.rs.utils.StringUtils;

/**
 * @author mayjors
 * 2017年9月4日
 */
@RestController
@RequestMapping("/")
public class UserController {
	static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

	@Resource
	private UserService userService;
	@Resource
	private UserTokenService userTokenService;

	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/add",
			consumes={"application/json"},produces={"application/json"})
	public Response add(@RequestBody UserVO uservo, HttpServletRequest request){
		log.debug("add user ");
		Response response = new Response();
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				//String parentUser = "sysadmin";//SessionManager.getCurrentUser(token).getUserId();
				String[] parts = token.split(":");
				String parentUser = parts[0];
				if(uservo.getUserId() == null) {
					response.setResultCode(-1);
					response.setDescription("name is null");
				} else {
					uservo.setParentUserId(parentUser);
					userService.addUser(uservo);
					response.setDescription("Succuss");
				}
			} else {
				uservo.setParentUserId("sysadmin");
				userService.addUser(uservo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
			response.setDescription("Fail");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/edit",
			consumes={"application/json"},produces={"application/json"})
	public Response update(@RequestBody UserVO uservo){
		log.debug("update user ");
		Response response = new Response();
		try {
			if(uservo.getUserId() == null) {
				response.setResultCode(-1);
				response.setDescription("userId is null");
			} else {
				userService.updateUserVO(uservo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response.setResultCode(-1);
			response.setDescription("Fail");
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.GET,path="/v1/security/user/detail")
	public VoResponse<UserVO> get(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("get user ");
		VoResponse<UserVO> response = new VoResponse<UserVO>(0);
		UserVO uservo = null;
		try {
			long idLong = StringUtils.handleLongParam(id);
			if(id != null) {
				uservo = userService.getUserVO(idLong);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse<UserVO>(-1);
		}

		response.setVo(uservo);
		return response;
	}

	@RequestMapping(method=RequestMethod.GET,path="/v1/security/user/list")
	public ListResponse<UserVO> getList(@RequestParam(value="name",required=false) String name,
                                        @RequestParam(value="cpid",required=false) String cpid,
                                        @RequestParam(value="spid",required=false) String spid,
                                        @RequestParam(value="enable",required=false) String enable,
                                        @RequestParam(value = "first",required=false) String first,
                                        @RequestParam(value = "max",required=false) String max,
                                        HttpServletRequest request){
		log.debug("get user list ");
		ListResponse<UserVO> response = new ListResponse<UserVO>(0);
		name = StringUtils.handleStrParam(name);
		cpid = StringUtils.handleStrParam(cpid);
		spid = StringUtils.handleStrParam(spid);
		enable = StringUtils.handleStrParam(enable);
		Integer firstInt = 0;
		Integer maxInt = -1;
		firstInt = StringUtils.handleIntParam(first)==null ? 0 : StringUtils.handleIntParam(first);
		maxInt = StringUtils.handleIntParam(max)== null ? -1 : StringUtils.handleIntParam(max);

		//long total = 0;
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				User currentUser = userTokenService.getCurrentUser(token);

				QueryResult<UserVO> queryResult = userService.getQuery(name, cpid,
						spid, enable,currentUser.getUserId(),firstInt, maxInt);
				if(queryResult != null) {
					response.setList(queryResult.getResults());
					response.setTotal(queryResult.getCount());
				}
			}else{
				log.error("token is null ");
				response = new ListResponse<UserVO>(-1);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new ListResponse<UserVO>(-1);
		}

		return response;
	}

	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/remove",
			consumes={"application/json"},produces={"application/json"})
	public Response delete(@RequestBody List<Long> ids){
		log.debug("remove user ");
		Response response = new Response(0);
		try {
			//			if (ids.size()> 0) {
			//				Long[] longArray = new Long[ids.size()];
			//				for(int i=0,j=ids.size();i<j;i++){
			//					longArray[i]=ids.get(i);
			//				}
			//				userService.removeUser(User.class, longArray);
			//			}
			for(Long id : ids){
				userService.removeUser(User.class, id);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(StatusCode.UI.UI_1);
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/enable")
	public Response enable(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("enable user ");
		Response response = new Response(0);
		//boolean enable = true;
		try {
			if(id != null) {
				long idLong = StringUtils.handleLongParam(id);
				User user = userService.getUser(idLong);
				if(user != null) {
					userService.enableUser(user);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(StatusCode.UI.UI_1);
		}
		//response.setVo(enable);
		return response;
	}

	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/password/reset")
	public VoResponse reset(@RequestParam("id") String id, HttpServletRequest request){
		log.debug("reset user password");
		VoResponse response = new VoResponse(StatusCode.UI.UI_0);
		try {
			if(id != null) {
				long idLong = StringUtils.handleLongParam(id);
				User user = userService.getUser(idLong);
				if(user != null) {
					userService.resetPassword(user);
					response.setVo(AESUtil.decrypt(user.getPassword()));
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse(StatusCode.UI.UI_1);
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.POST,path="/v1/security/user/password/edit")
	public Response editPws(@RequestBody Map<String,String> body, HttpServletRequest request){
		log.debug("reset user password");
		Response response = new Response(0);
		try {
			String token = request.getHeader("Token");
			if (token != null) {
				User currentUser = userTokenService.getCurrentUser(token);
				if (currentUser != null) {
					String oldPw = body.get("oldPw");
					String newPw = body.get("newPw");
					if (oldPw.equals(AESUtil.decrypt(currentUser.getPassword()))) {
						currentUser.setPassword(AESUtil.encrypt(newPw));
						//修改密码时更新密码修改时间
						currentUser.setPwdUpdateTime(new Date());
						userService.updateUser(currentUser);
					} else {
						response.setResultCode(StatusCode.UI.UI_OLD_PASWORD_ERROR);
						response.setDescription("the oldPw is error! ");
					}
				}
			} else {
				response.setResultCode(-1);
				response.setDescription("the token is null! ");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new Response(StatusCode.UI.UI_0);
		}
		return response;
	}

	@RequestMapping(method=RequestMethod.GET,path="/v1/security/user/check/unique")
	public VoResponse<Boolean> judgeUnique(@RequestParam("name") String name,
			HttpServletRequest request){
		log.debug("judge Unique userName ");
		VoResponse<Boolean> response = new VoResponse<Boolean>(0);
		UserVO uservo = null;
		boolean unique = false;
		try {
			name = StringUtils.handleStrParam(name);
			if(name != null) {
				uservo = userService.getUserVOByUserId(name);
				unique = uservo == null? true:false;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			response = new VoResponse<Boolean>(-1);
		}

		response.setVo(unique);
		return response;
	}

}
