package com.hoob.rs.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HTTP工具类
 * 
 * @author makefu
 * @date 2017年2月16日
 */
public class HttpUtils {

	static final Logger LOG = LogManager.getLogger(HttpUtils.class);
	private static final int TIMEOUT = 60000; //60S

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址, 
	 * 
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？ 
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。 
	 * 
	 * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 
	 * 192.168.1.100 
	 * 
	 * 用户真实IP为： 192.168.1.110 
	 * 
	 * @param request 
	 * @return 
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 发送post消息并返回对方回复的内容
	 * 
	 * @param url
	 * @param contentw
	 * @return
	 */
	public static Response post(String url, String json) {

		final int TIMEOUT = 1000 * 60;// 发送post消息的超时时间

		LOG.info("post - url = {}, json = {}", url, json);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("POST");
			uc.setRequestProperty("content-type", "application/json");
			// uc.setRequestProperty("content-type","application/x-www-form-urlencoded");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			// 此处要对流指定字符集，否则服务端接收会有中文件乱码
			if (json != null) {
				osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
				osw.write(json);
				osw.flush();
			}

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Response response = new Response(responseCode, sb.toString());
		LOG.info("get - response = {}", response);
		return response;
	}

	/**
	 * 发送Post消息，内容以application/xml的形式
	 * 
	 * @author faker
	 * @param url
	 * @param xml
	 * @return
	 */
	public static Response post4xml(String url, String xml) {
		final int TIMEOUT = 1000 * 60;// 发送post消息的超时时间

		LOG.debug("post - url = {}, xml = {}", url, xml);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("POST");
			uc.setRequestProperty("content-type", "application/xml");
			// uc.setRequestProperty("content-type","application/x-www-form-urlencoded");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setRequestProperty("Accept", "application/xml");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);
			// 此处要对流指定字符集，否则服务端接收会有中文件乱码
			if (xml != null) {
				osw = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
				osw.write(xml);
				osw.flush();
			}

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}

				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Response response = new Response(responseCode, sb.toString());
		LOG.debug("get - response = {}", response);
		return response;
	}

	/**
	 * 发送get消息并返回对方回复的内容
	 * 
	 * @param url
	 * @return
	 */
	public static Response get(String url) {

		final int TIMEOUT = 1000 * 5;// 发送get消息的超时时间

		LOG.debug("get - url = {}", url);

		StringBuffer sb = new StringBuffer();
		int responseCode = -1;
		OutputStreamWriter osw = null;
		BufferedReader in = null;
		try {
			URL urls = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) urls.openConnection();
			uc.setRequestMethod("GET");
			uc.setRequestProperty("charset", "UTF-8");
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);

			// 接收返回消息时要指定流的字符集，否则会有中文乱码
			InputStream is = uc.getInputStream();
			if (is != null) {
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String readLine = "";
				while ((readLine = in.readLine()) != null) {
					sb.append(readLine);
				}
				in.close();
			}
			responseCode = uc.getResponseCode();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Response response = new Response(responseCode, sb.toString());
		LOG.debug("get - response = {}", response);
		return response;
	}
	/****/
	public static class Response {
		// -1未知异常
		private int responseCode;
		private Object content;
		/****/
		public Response(int responseCode, Object content) {
			super();
			this.responseCode = responseCode;
			this.content = content;
		}

		public int getResponseCode() {
			return responseCode;
		}

		public Object getContent() {
			return content;
		}

		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}

		public void setContent(Object content) {
			this.content = content;
		}

		@Override
		public String toString() {
			return "Response [responseCode=" + responseCode + ", content=" + content + "]";
		}

	}


	/**
	 * 发送消息到CDN
	 * @param xmlURL
	 * @return
	 */
	public static Response sendSoapMessage(String target,SOAPMessage message) {
		Response resp = new Response(404,null);		
		if(StringUtils.isEmpty(target)){
			LOG.error("Send target is null or empty");
			return resp;
		}
		SOAPConnection connection = null;
		try {
			// 创建连接  
			SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();  
			connection = soapConnFactory.createConnection();  

			URL url = new URL(target);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			//uc.setRequestMethod("GET");
			//uc.setRequestProperty("charset", "UTF-8");
			//uc.setDoOutput(true);
			//uc.setDoInput(true);
			uc.setReadTimeout(TIMEOUT);
			uc.setConnectTimeout(TIMEOUT);            

			// 响应消息  
			SOAPMessage reply = connection.call(message, uc.getURL());

			// 创建soap消息转换对象  
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();  
			// Extract the content of the reply
			Source sourceContent = reply.getSOAPPart().getContent();  
			// Set the output for the transformation  

			LOG.debug("SOAP Message-->{}",sourceContent);                   

			resp.setContent(reply);
			resp.setResponseCode(200);

			return resp;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			resp.setContent(e.getMessage());
			resp.setResponseCode(500);			
		}finally{
			try {
				// Close the connection 关闭连接	             
				if(connection!=null) {
					connection.close();
				}
			} catch (SOAPException e) {	
				LOG.error(e.getMessage(), e);				
			}	
		}
		return resp;
	}





}
