package com.hoob.rs.security.controller;

import com.hoob.rs.comm.vo.ListResponse;
import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.comm.vo.VoResponse;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.service.PrivilegeService;
import com.hoob.rs.security.service.UserService;
import com.hoob.rs.security.vo.PrivilegeVO;
import com.hoob.rs.utils.SessionManager;
import com.hoob.rs.utils.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mayjors
 * 2017年9月8日
 */
@RestController
@RequestMapping("/")
public class PrivilegeController {
    static Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    PrivilegeService privilegeService;
    @Resource
    UserService userService;

    @RequestMapping(method= RequestMethod.POST,path="/v1/security/privilege/add", consumes={"application/json"},produces={"application/json"})
    public Response add(@RequestBody PrivilegeVO privilegeVO){
        log.debug("add privilege ");
        Response response = new Response();
        try {
            if(privilegeVO.getMenuCode() == null) {
                response.setResultCode(-1);
                response.setDescription("code is null");
            } else {
                privilegeService.addPrivilege(privilegeVO);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setResultCode(-1);
            response.setDescription("Fail");
        }
        return response;
    }

    @RequestMapping(method=RequestMethod.GET,path="/v1/security/privilege/detail")
    public VoResponse<PrivilegeVO> get(@RequestParam("id") String id, HttpServletRequest request){
        log.debug("get role ");
        VoResponse<PrivilegeVO> response = new VoResponse<PrivilegeVO>(0);
        PrivilegeVO privilegeVO = null;
        try {
            if(id != null) {
                long idLong = StringUtils.handleLongParam(id);
                privilegeVO = privilegeService.getPrivilegeVo(idLong);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        response.setVo(privilegeVO);
        return response;
    }

    @RequestMapping(method=RequestMethod.GET,path="/v1/security/privilege/list")
    public ListResponse<PrivilegeVO> getList(@RequestParam(value="menuCode",required=false) String menuCode,@RequestParam(value="menuLevel",required=false) String menuLevel,
                                @RequestParam(value="parent",required=false) String parent,@RequestParam(value="enable",required=false) String enable,
                                @RequestParam(value = "first",required=false) String first, @RequestParam(value = "max",required=false) String max,
                                HttpServletRequest request){
        log.debug("get privilege list ");
        ListResponse<PrivilegeVO> response = new ListResponse<PrivilegeVO>(0);
        // menuCode = StringUtils.handleStrParam(menuCode);
        //menuLevel = StringUtils.handleStrParam(menuLevel);
        parent = StringUtils.handleStrParam(parent);
        //enable = StringUtils.handleStrParam(enable);
        //Integer firstInt = 0;
       // Integer maxInt = -1;
       // firstInt = StringUtils.handleIntParam(first)==null ? 0 : StringUtils.handleIntParam(first);
       // maxInt = StringUtils.handleIntParam(max)== null ? -1 : StringUtils.handleIntParam(max);
        //Integer parent_int = StringUtils.handleIntParam(parent);

        //long total = 0;
        try {
            List<PrivilegeVO> pList = new ArrayList<PrivilegeVO>();
            String token = request.getHeader("Token");

            if (token != null) {
                String currentUser = SessionManager.getCurrentUser(token).getUserId();
                User user = userService.getUserByUserId(currentUser);
                pList = privilegeService.getPrivileges(user);
            } else {
                User user = userService.getUserByUserId("sysadmin");
                pList = privilegeService.getPrivileges(user);
            }
            response.setList(pList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = new ListResponse<PrivilegeVO>(-1);
        }
        return response;
    }
}
