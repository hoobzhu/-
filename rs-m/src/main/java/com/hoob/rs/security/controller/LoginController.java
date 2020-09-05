package com.hoob.rs.security.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.comm.vo.VoResponse;
import com.hoob.rs.constants.StatusCode;
import com.hoob.rs.security.model.User;
import com.hoob.rs.security.service.LoginService;
import com.hoob.rs.security.service.UserService;
import com.hoob.rs.security.service.UserTokenService;
import com.hoob.rs.security.vo.LoginRequest;
import com.hoob.rs.security.vo.LoginResponse;
import com.hoob.rs.security.vo.UserSession;
import com.hoob.rs.sys.init.SecurityUtils;
import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.utils.AESUtil;
import com.hoob.rs.utils.RSAUtil;
import com.hoob.rs.utils.SessionManager;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


@RestController
@RequestMapping
public class LoginController {
	
	static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
		
	@Resource(name = "imageCaptchaService")  
    private ImageCaptchaService imageCaptchaService;
	
	@Resource
	LoginService loginService;
	@Resource
	private UserService userService;
	@Resource
	private UserTokenService userTokenService;
	@Resource
	private SysConfigService sysConfigService;
	
	/**
	 * 获取验证码
	 * @param requestId
	 * @param language
	 * @return
	 */
    @RequestMapping(method=RequestMethod.GET,path="/v1/generate/captcha",produces="image/jpg")
    public byte[] ImageCaptcha(@RequestParam(name="requestid") String requestId,@RequestParam(name="language") String language){  
      	
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();  
        try {            
            BufferedImage challenge = imageCaptchaService.getImageChallengeForID(requestId, new Locale(language));  
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);              
            jpegEncoder.encode(challenge);  
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
          
       return jpegOutputStream.toByteArray();            
    }
    
    
    /**
     * 用户登录
     * @param login
     * @return
     */
    @RequestMapping(method=RequestMethod.POST,path="/v1/login", consumes={"application/json"},produces={"application/json"})
    public LoginResponse login(@RequestBody LoginRequest login){
    	LoginResponse resp = new LoginResponse();
    	logger.debug("captcha ---- > " + login.getCaptcha());
    	try{
    		boolean captchaIsRight = imageCaptchaService.validateResponseForID(login.getRequestId(),login.getCaptcha());
    		if(!captchaIsRight){
        		resp.setResultCode(StatusCode.UI.UI_20003); //验证码不正确
        		return resp;
        	} 
    	}catch(CaptchaServiceException e){
    		logger.error(e.getMessage(),e);
    		resp.setResultCode(StatusCode.UI.UI_20003); //验证码不正确
    		return resp;
    	}
    	
    	String decryptPwd = "";
		try {
			Map<String, RSAKey> rsaKeys = RSAUtil.getByUser(login.getUserId());
			RSAPrivateKey privateKey = (RSAPrivateKey) rsaKeys.get(RSAUtil.PRIVATE_KEY);
			decryptPwd = RSAUtil.decrypt(login.getPassword(), privateKey);
		} catch (Exception e) {
			logger.error("", e);
		}
    	
    	UserSession session = null;
		try {
			session = loginService.authorize(login.getUserId(), decryptPwd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session = null;
		}
    			
    	if(null==session){
    		resp.setResultCode(StatusCode.UI.UI_20005); //认证不通过
    		return resp;
    	}else{
    		String token = SecurityUtils.createToken(login.getUserId(), decryptPwd, session.getLastActiveTime().getTime());
    		SessionManager.addSessionUser(token, session);
    		User user = userService.getUserByUserId(session.getUserId());
    		//未启用用户
    		if(!user.getEnable()){
    			 resp.setResultCode(StatusCode.UI.UI_20012); 
    			 return resp;
    		}
    		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    		String ip = request.getRemoteAddr();
    		logger.info("Login from ip address:{}",ip);
    		if(user.getEnableIPBinding()&&!isAllowVisit(user.getIpAddr(), ip)){
    			resp.setResultCode(StatusCode.UI.UI_20001); //IP限制
    		}else{
    			// 更新用户登录时间和次数
    			userService.updateUserLoginInfo(user);  
    			resp.setRole(user.getRole());
        		resp.setResultCode(StatusCode.UI.UI_0); 
        		resp.setToken(token);
        		resp.setPrivileges(session.getMenuPrivileges());
    		}    		
    	}    	
    	
    	return resp;
    }
    
    /**
     * 用户Logout
     * @return
     */
    @RequestMapping(method=RequestMethod.DELETE,path="/v1/logout",produces={"application/json"})
    public Response logout(){    	
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    	String token = request.getHeader("Token");    	
    	if(null!=token){    		
    		SessionManager.removeSessionUser(token);
    	}    	
    	Response resp = new Response();
    	resp.setResultCode(StatusCode.UI.UI_0);
    	return resp;
    }    

    
    /**
     * 校验登录IP是否在允许登录的白名单中
     * @param restrictIp
     * @param remoteIp
     * @return
     */
    private boolean isAllowVisit(String restrictIp,String remoteIp){
    	if(StringUtils.isEmpty(restrictIp)) {
            return true;
        }
    	if(StringUtils.isEmpty(remoteIp)) {
            return false;
        }
    	
    	String[] whiteList = restrictIp.split(",");
    	for(String item : whiteList){
    		if(remoteIp.trim().equals(item.trim())) {
                return true;
            }
    	}    	
    	return false;
    }
    
    
	
	@RequestMapping(method=RequestMethod.GET,path="/v1/security/check/default/password")
	public VoResponse<Boolean> checkDefaultPassword(HttpServletRequest request){
		VoResponse<Boolean> rsp = new VoResponse<Boolean>(StatusCode.UI.UI_0);
		try {
			String token = request.getHeader("Token");
			User user = userTokenService.getCurrentUser(token);
			if("9db06bcff9248837f86d1a6bcf41c9e7".equals(AESUtil.decrypt(user.getPassword()))){
				rsp.setVo(true);
			}else{
				rsp.setVo(false);
			}
			if(!sysConfigService.checkPassWordTimeOut(user.getPwdUpdateTime())){
				rsp.setResultCode(StatusCode.UI.UI_PWD_60001);//密码过期
			}
			return rsp;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rsp = new VoResponse<Boolean>(StatusCode.UI.UI_1);
		}
		return rsp;
	}
	
	/**
	 * 获取RSA加密公钥
	 *
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/v1/security/publicKey")
	public VoResponse<String> gainPublicKey(@RequestParam(name = "userId") String userId) {
		VoResponse<String> resp = new VoResponse<>(StatusCode.UI.UI_0);
		try {
			User user = userService.getUserByUserId(userId);
			if (user == null) {
				logger.error("user:{} does not exist.", userId);
				resp.setResultCode(StatusCode.UI.UI_20005);
				return resp;
			}
			//
			Map<String, RSAKey> keys = RSAUtil.getKeys();
			RSAUtil.addByUser(userId, keys);
			RSAPublicKey publicKey = (RSAPublicKey) keys.get(RSAUtil.PUBLIC_KEY);
			String publicKeyStr = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
			resp.setVo(publicKeyStr);
			return resp;
		} catch (Exception e) {
			logger.error("", e);
			resp.setResultCode(StatusCode.UI.UI_20005);
			return resp;
		}
	}
}
