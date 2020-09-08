/**
 * 
 */
package com.hoob.rs.sys.init;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.hoob.rs.comm.vo.Response;
import com.hoob.rs.constants.StatusCode;
import com.hoob.rs.sys.model.SysConfig;
import com.hoob.rs.sys.service.SysConfigService;
import com.hoob.rs.sys.service.SysConfigServiceImpl;
import com.hoob.rs.utils.HttpUtils;
import com.hoob.rs.utils.JsonUtils;

/**
 * @author Raul 2017年8月29日
 */
public class SystemSecurityFilter implements Filter {

	Logger log = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	private List<String> whiteList = new ArrayList<String>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		
		/**
		 * 处理前端跨域问题
		 */
		// 指定允许其他域名访问
		resp.setHeader("Access-Control-Allow-Origin", "*");
		// 响应类型
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");
		// 响应头设置
		resp.setHeader("Access-Control-Allow-Headers", 
				"Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,"
				+ "Access-Control-Request-Headers,token");
		
		
		String uri = req.getRequestURI().replaceFirst(req.getContextPath(), "");
		Iterator<String> ite = whiteList.iterator();
		while (ite.hasNext()) {
			String patternString = ite.next();
			// 正则规则
			Pattern pattern = Pattern.compile(patternString);
			// 被校验的字符串
			Matcher match = pattern.matcher(uri);
			if (match.matches()) {
				chain.doFilter(req, resp);								
				return;
			}
		}
		// CommonsMultipartResolver multipartResolver = new
		// CommonsMultipartResolver(request.getServletContext());
		// if(multipartResolver.isMultipart(req)) chain.doFilter(req,
		// resp);//temp

		BodyReaderHttpServletRequestWrapper warpper = new BodyReaderHttpServletRequestWrapper(req);
		String body = warpper.getRequestBody();

		// HMAC校验
//		if (!checkHMAC(req, body)) {
//			close(resp, StatusCode.UI.UI_20009);
//			return;
//		}

	
		chain.doFilter(warpper, response);
	}
	
	
	
	
	
	

	@Override
	public void destroy() {

	}


	/**
	 * HMAC校验（防篡改）
	 * 
	 * @param request
	 * @param requestBody
	 * @return
	 */
	private boolean checkHMAC(HttpServletRequest request, String requestBody) {

		if (null == request) {
            return false;
        }
		if (null == requestBody) {
            requestBody = "";
        }
		String clientHmac = request.getHeader("Hmac");
		if (null == clientHmac || clientHmac.trim().isEmpty()) {
            return true; // 认为不开启好了
        }

		String token = request.getHeader("Token");
		String terminalType = request.getHeader("Terminal-Type");
		String deviceId = request.getHeader("Device-Id");
		String timestamp = request.getHeader("Timestamp");
		String timezone = request.getHeader("Timezone-Offset");
		String random = request.getHeader("Random");
		String serviceId = request.getHeader("Service-Id");

		StringBuilder strb = new StringBuilder();
		strb.append("{\"Header\":{\"Token\":\"").append(token).append("\",").append("\"Terminal-Type\":\"")
				.append(terminalType).append("\",").append("\"Device-Id\":\"").append(deviceId).append("\",")
				.append("\"TS1\":\"").append(timestamp).append("\",").append("\"TSZ\":\"").append(timezone)
				.append("\",").append("\"RND\":\"").append(random).append("\",").append("\"Service-Id\":\"")
				.append(serviceId).append("\"}");
		strb.append(",");
		if (null == requestBody || requestBody.trim().isEmpty()) {
			strb.append("\"Body\":").append("{}");
		} else {
			strb.append("\"Body\":").append(requestBody).append("");
		}
		strb.append("}");
		// KEY 文件头加密 生成HMAC
		String key = null;
		try {
			key = SecurityUtils.createMD5(SecurityUtils.createMD5(token).toUpperCase()+ token.substring(5, 10)).toUpperCase();
		} catch (Exception e) {
			log.error(e);
		}

		// 服务端产生hmac
		String hmac = SecurityUtils.calculateHMAC(strb.toString(), key);
		return clientHmac.equals(hmac);
	}

	/**
	 * 拦截请求，反馈异常
	 * 
	 * @param response
	 * @param resultCode
	 */
	private void close(HttpServletResponse response, int resultCode) {
		Response respVO = new Response();
		respVO.setResultCode(resultCode);
		PrintWriter pw;
		try {
			response.setStatus(200);
			response.setContentType("application/json");
			pw = response.getWriter();
			pw.write(JsonUtils.obj2Json(respVO));
		} catch (IOException e) {
			log.error(e);
		}

	}
	
	
	
	
	
	

}
