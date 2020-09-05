/**
 * 
 */
package com.hoob.rs.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hoob.rs.sys.vo.FtpServerVO;

/**
 * @author raul
 *
 */
public class FTPUtils {

	private final static Logger log = LogManager
			.getLogger(LogManager.ROOT_LOGGER_NAME);

	private static final Map<String, FTPClient> FTP_SESSION = new HashMap<String, FTPClient>();
	private static final Map<String, Long> FTP_SESSION_AVAILABLE = new HashMap<String, Long>(); // 记录FTP
																								// Client最近活跃时间
	private static final long MAX_WAIT_TIME = 2 * 3600 * 1000; // 两小时没有下载任务后释放连接

	private static final int defaultTimeout = 10000; // ms
	private static final String defaultEncoding = "UTF-8";
	/**
	 * 定时器检查FTP Session是否超时，如果超时释放FTP连接
	 */
	static {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				log.info("i'm running:)");
				release();
			}
		}, 0, 30000); // 30秒
	}

	/**
	 * 获取一个FTP Client连接
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static FTPClient createFTPClient(String ip, int port, String user,
			String password) throws Exception {
		FTPClient client = new FTPClient();
		client.setControlEncoding("UTF-8");
		client.connect(ip, port);
		client.login(user, password);
		client.enterLocalPassiveMode();
		client.setFileType(FTP.BINARY_FILE_TYPE);
		client.setConnectTimeout(10000);
		client.setDataTimeout(10000);
		client.setControlKeepAliveTimeout(24 * 3600 * 1000);
		client.setControlKeepAliveReplyTimeout(10000);
		return client;
	}

	/**
	 * 校验下载地址是否符合FTP URL规则
	 * 
	 * @param url
	 * @return
	 */
	public static FTPDownloadInfo parseFTPDownloadURL(String url) {
		if (null == url) {
			return null;
		}
		url = url.trim();
		String reg = "^ftp://(.+):(.*)@([^\\s^:]+):([1-9]+)(/*.*)"; // ftp://user:password@host:port/xx
		boolean r = Pattern.matches(reg, url);
		FTPDownloadInfo info = new FTPUtils().new FTPDownloadInfo();
		if (r) {
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(url);
			if (m.find()) {
				info.setAccount(m.group(1));
				info.setPassword(m.group(2));
				info.setIp(m.group(3));
				int port = 21;

				String temp = m.group(4);
				if (m.group(4).contains("/")) {
					temp = temp.substring(0, temp.indexOf("/"));
				}
				if (null != temp && !temp.isEmpty()) {
					try {
						port = Integer.parseInt(temp);
					} catch (Exception ex) {
						log.error(ex);
						port = 21;
					}
				}
				info.setPort(port);
				info.setFile(m.group(5));
				return info;
			}
		} else {
			reg = "^ftp://(.+):(.*)@([^\\s^:^//]+)(/*.*)";// ftp://user:password@host/xx
			r = Pattern.matches(reg, url);
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(url);
			if (r) {
				if (m.find()) {
					info.setAccount(m.group(1));
					info.setPassword(m.group(2));
					info.setIp(m.group(3));
					info.setPort(21);
					info.setFile(m.group(4));
					return info;
				}
			}
		}
		log.warn("FTP download file["
				+ url
				+ "] format is not correct,"
				+ "correct format:ftp://user:password@host:port/a/b.jpg or ftp://user:password@host/a/b.jpg");
		return null;
	}

	/**
	 * 下载文件保存到本地
	 * 
	 * @param host
	 * @param port
	 * @param user
	 * @param password
	 * @param remoteFile
	 * @param localFile
	 * @return
	 */
	public synchronized static boolean downloadFile(String host, int port,
			String user, String password, String remoteFile, String localFile) {
		log.debug("remoteFile:" + remoteFile);
		log.debug("localFile:" + localFile);
		boolean success = false;
		FTPClient ftpClient = null;
		OutputStream outputStream = null;
		String temp = "";
		int replyCode = 0;
		try {
			temp = "ftp://" + user + ":" + password + "@" + host + ":" + port
					+ remoteFile;
			String key = "host:" + host + ",port:" + port + ",user:" + user
					+ ",password:" + password;
			ftpClient = createFTPClient(host, port, user, password);
			if (StringUtils.isEmpty(localFile)) {
				return false;
			}
			File file = new File(localFile.substring(0,
					localFile.lastIndexOf(File.separator)));
			if (!file.exists()) {
				file.mkdirs();
			}
			outputStream = new BufferedOutputStream(new FileOutputStream(
					localFile));
			remoteFile = remoteFile.substring(1, remoteFile.length());

			success = ftpClient.retrieveFile(
					new String(remoteFile.getBytes("UTF-8"), "ISO-8859-1"),
					outputStream);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("ftpfile download fail,url:" + temp, ex);
		} finally {
			// 让服务端关闭连接 或 监视线程等待CLIENT超过最长活跃期关闭连接
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ftpClient != null && ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
					if (ftpClient.isConnected()) {
						ftpClient.logout();
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}

	/**
	 * 下载文件保存到本地
	 * 
	 * @param source
	 * @param localFile
	 * @return
	 */
	public synchronized static boolean downloadFile(String source,
			String localFile) {
		FTPDownloadInfo info = parseFTPDownloadURL(source);
		if (null == info) {
			return false;
		}
		return downloadFile(info.getIp(), info.getPort(), info.getAccount(),
				info.getPassword(), info.getFile(), localFile);
	}

	/**
	 * 下载文件保存到本地指定目录,区别于downloadFile
	 * 
	 * @param source
	 * @param localFilePath
	 * @return
	 */
	public static boolean downloadFile2localPath(String source,
			String localFilePath) {
		String localFile = createLocalFile(source, localFilePath);
		return downloadFile(source, localFile);
	}

	/**
	 * 从源地址和目标目录组装目标文件地址
	 * 
	 * @param source
	 * @param localFilePath
	 * @return
	 */
	public static String createLocalFile(String source, String localFilePath) {
		File path = new File(localFilePath);
		if (!path.exists() || !path.isDirectory()) {
			path.mkdirs();
		}
		String fileName = source.substring(source.lastIndexOf("/") == -1 ? 0
				: source.lastIndexOf("/") + 1);
		fileName = fileName.substring(fileName.lastIndexOf("\\") == -1 ? 0
				: fileName.lastIndexOf("\\") + 1);// 兼容Windows的\，防止给一个Windows的地址解析出错
		String localFile = localFilePath
				+ ((localFilePath.endsWith("\\") || localFilePath.endsWith("/")) ? ""
						: File.separator) + fileName;
		return localFile;
	}

	/**
	 * 超过MAX_WAIT_TIME释放FTPClient.
	 */
	public static void release() {
		Set<String> keys = FTP_SESSION.keySet();
		if (null == keys || keys.isEmpty()) {
			return;
		}
		Iterator<String> it = keys.iterator();
		if (null == it) {
			return;
		}
		while (it.hasNext()) {
			String key = it.next();
			long time = FTP_SESSION_AVAILABLE.get(key);
			FTPClient client = FTP_SESSION.get(key);
			if (System.currentTimeMillis() - time > MAX_WAIT_TIME) {
				it.remove();
				FTP_SESSION.remove(key);
				FTP_SESSION_AVAILABLE.remove(key);
				try {
					if (client != null && client.isConnected()) {
						log.info("Release ftp client:" + key);
						client.logout();
						client.disconnect();
					}
				} catch (IOException ex) {
					log.error(ex);
				}
			}
		}
	}

	/**
	 * 连接并登录FTP服务器。返回的{@linkplain FTPClient}对象使用完毕后，必须通过
	 * {@linkplain #closeFtp(FTPClient)}方法断开连接。
	 * 
	 * @return 如果连接并登录FTP服务器成功，返回FTPClient对象；否则，返回null。
	 */
	public static FTPClient openFtp(FtpServerVO ftpInfo) {
		FTPClient ftp = new FTPClient();
		// Set the default timeout in milliseconds to use when opening a
		// socket,used previous to a call to connect()
		ftp.setDefaultTimeout(defaultTimeout);
		// Sets the connection timeout in milliseconds, which will be passed to
		// the Socket object's connect() method.
		ftp.setConnectTimeout(defaultTimeout);
		/*
		 * ftp.setControlEncoding(ftpInfo.getEncoding() == null ||
		 * ftpInfo.getEncoding().equals("") ? defaultEncoding : ftpInfo
		 * .getEncoding());
		 */
		ftp.setControlEncoding(defaultEncoding);
		ftp.addProtocolCommandListener(new PrintCommandListener(
				new PrintWriter(System.out)));
		try {
			ftp.connect(
					ftpInfo.getHostIp(),
					ftpInfo.getPort() == 0 ? FTP.DEFAULT_PORT : ftpInfo
							.getPort());
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				closeFtp(ftp);
				return null;
			}

			if (!ftp.login(ftpInfo.getUsername(), ftpInfo.getPassword())) {
				closeFtp(ftp);
				return null;
			}
			// RequestContext.getCurrentInstance().addCallbackParam("ftpParam",
			// "connect");
			// setSysType(ftp.getSystemType());

			return ftp;
		} catch (SocketException e) {
			log.error("Connect ftp server:" + ftpInfo.getHostIp()
					+ " Socket Error!");
			closeFtp(ftp);
		} catch (IOException e) {
			log.error("Connect ftp server:" + ftpInfo.getHostIp()
					+ " IOException!");
			closeFtp(ftp);
		}

		return null;
	}

	/**
	 * 断开与FTP服务器的连接。一定要保证使用完毕后调用此方法关闭与服务器的连接。
	 * 
	 * @param ftp
	 *            由{@linkplain #openFtp()}打开的FTP连接
	 */
	public static void closeFtp(FTPClient ftp) {
		if (ftp != null) {
			try {
				ftp.disconnect();
				log.info("FTP disconnected.");
			} catch (IOException e) {
				// log.info("Close ftp server:" + ftpInfo.getHost() +
				// " IOException!");
			}
		}
	}

	/**
	 * 列出服务器上指定目录下所有满足过滤条件的内容。
	 * 
	 * @param path
	 *            路径。
	 * @param filter
	 *            过滤器。
	 * @return 返回满足条件的内容列表。如果失败，返回null。
	 */
	public static FTPFile[] list(FtpServerVO ftpIn, String path,
			FTPFileFilter filter) {
		FTPClient ftp = openFtp(ftpIn);
		try {
			if (ftp != null) {
				return ftp.listFiles(path, filter);
			}
		} catch (SocketException e) {

		} catch (IOException e) {

		} catch (Exception e) {

		} finally {
			closeFtp(ftp);
		}
		return null;
	}

	public static FTPFile[] listFiles(String ftpPath) {
		FTPClient ftp = null;
		try {
			FTPDownloadInfo ftpInfo = FTPUtils.parseFTPDownloadURL(ftpPath);
			if (ftpInfo == null)
				return null;
			FtpServerVO ftpvo = new FtpServerVO();
			ftpvo.setHostIp(ftpInfo.getIp());
			ftpvo.setPort(ftpInfo.getPort());
			ftpvo.setUsername(ftpInfo.getAccount());
			ftpvo.setPassword(ftpInfo.getPassword());
			ftp = openFtp(ftpvo);
			if (ftp != null) {
				String file = ftpInfo.getFile();
				if(StringUtils.isEmpty(file)) return null;
				if(file.startsWith("/") && !"/".equals(file)){
					file = file.substring(1);
				}
				if (ftp.changeWorkingDirectory(file)) {
					return ftp.listFiles();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (ftp != null) {
				closeFtp(ftp);
			}
		}
		return null;
	}

	/**
	 * 判断文件名是否有效。
	 * 
	 * @param fileName
	 *            文件名。
	 * @return 如果文件名有效，返回true；否则，返回false。
	 */
	public static final boolean isValidFileName(String regex, String fileName) {
		if (fileName != null) {
			Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher ma = pa.matcher(fileName);
			if (ma.matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对将用于FTP URL的文件路径进行必要的编码。
	 * 
	 * @param path
	 *            文件路径。
	 * @return 如果成功，返回经编码的文件路径；否则，返回原来的路径。
	 */
	public static final String encodePath(String path) {
		String rootPath = "/";
		if (path.startsWith(rootPath)) {
			return urlEncode(rootPath) + encodePath(path.substring(1));
		} else {
			String[] names = path.split("/");
			StringBuilder sb = new StringBuilder();
			for (String name : names) {
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(urlEncode(name));
			}
			path = sb.toString();
		}
		return path;
	}

	public static final String urlEncode(String s) {
		String urlEncode = "utf-8";
		try {
			return URLEncoder.encode(s, urlEncode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 获取FTP URL前缀。
	 * 
	 * @param ftpServer
	 *            FTP服务器信息。
	 * @return 返回制定FTP服务器的URL。
	 */
	public static final String getFtpUrlPreffix(FtpServerVO ftpServer) {
		return String.format("ftp://%1$s:%2$s@%3$s:%4$s/",
				ftpServer.getUsername(), ftpServer.getPassword(),
				ftpServer.getHostIp(), ftpServer.getPort());
	}

	public static final String combine(String rootDir, String subDir) {
		if (StringUtils.isNotEmpty(rootDir)) {
			if (StringUtils.isNotEmpty(subDir)) {
				if (isAbsolutePath(subDir)) {
					return subDir;
				} else {
					char endWith = rootDir.charAt(rootDir.length() - 1);
					if (endWith == '/' || endWith == '\\') {
						return rootDir + subDir;
					} else {
						return rootDir + "/" + subDir;
					}
				}
			} else {
				return rootDir;
			}
		} else {
			return subDir;
		}
	}

	/**
	 * 判断一个路径或URL是否为绝对路径。
	 * 
	 * @param fileName
	 *            带路径文件名。
	 * @return 如果是绝对路径，返回true；否则，返回false。
	 */
	public static final boolean isAbsolutePath(String fileName) {
		Pattern protocolPrefix = Pattern.compile("^\\w+:.*$");
		Pattern drivePattern = Pattern.compile("^[a-zA-Z]:");
		if (StringUtils.isNotEmpty(fileName)) {
			if (fileName.startsWith("/") || fileName.startsWith("\\")
					|| protocolPrefix.matcher(fileName).find()
					|| drivePattern.matcher(fileName).find()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 读取ftp文件
	 * @param ftpPath  ftp://ftpuser:ftpuser@172.16.199.108/schedule/schedule.properties
	 * @return
	 */
	public static boolean writeFile(String content,String ftpPath) {
		if(StringUtils.isEmpty(ftpPath)) return false;
		String fileName = ftpPath.substring(ftpPath.lastIndexOf("/")+1, ftpPath.length());
		if(StringUtils.isEmpty(fileName)) return false;
		FTPClient ftp = null;
		InputStream is = null;  
		try {
			FTPDownloadInfo ftpInfo = FTPUtils.parseFTPDownloadURL(ftpPath);
			if (ftpInfo == null)
				return false;
			FtpServerVO ftpvo = new FtpServerVO();
			ftpvo.setHostIp(ftpInfo.getIp());
			ftpvo.setPort(ftpInfo.getPort());
			ftpvo.setUsername(ftpInfo.getAccount());
			ftpvo.setPassword(ftpInfo.getPassword());
			ftp = openFtp(ftpvo);
			if (ftp != null) {
				//切换目录
		        String fileDir = ftpInfo.getFile();
				if(StringUtils.isEmpty(fileDir)) return false;
				fileDir = fileDir.substring(0,fileDir.lastIndexOf("/"));
				if(fileDir.startsWith("/") && !"/".equals(fileDir)){
					fileDir = fileDir.substring(1);
				}
				ftp.changeWorkingDirectory(fileDir);
	            is = new ByteArrayInputStream(content.getBytes());  
	            // 5.写操作  
	            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
	            return ftp.storeFile(fileName, is);  
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (ftp != null) {
				closeFtp(ftp);
			}
			if(is !=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * 读取ftp文件
	 * @param ftpPath  ftp://ftpuser:ftpuser@172.16.199.108/schedule/schedule.properties
	 * @return
	 */
	public static String readFile(String ftpPath) {
		if(StringUtils.isEmpty(ftpPath)) return null;
		String fileName = ftpPath.substring(ftpPath.lastIndexOf("/")+1, ftpPath.length());
		if(StringUtils.isEmpty(fileName)) return null;
		StringBuffer sb = new StringBuffer();
		FTPClient ftp = null;
		BufferedReader br = null;
		InputStream is = null;
		try {
			FTPDownloadInfo ftpInfo = FTPUtils.parseFTPDownloadURL(ftpPath);
			if (ftpInfo == null)
				return null;
			FtpServerVO ftpvo = new FtpServerVO();
			ftpvo.setHostIp(ftpInfo.getIp());
			ftpvo.setPort(ftpInfo.getPort());
			ftpvo.setUsername(ftpInfo.getAccount());
			ftpvo.setPassword(ftpInfo.getPassword());
			ftp = openFtp(ftpvo);
			if (ftp != null) {
				//切换目录
		        String fileDir = ftpInfo.getFile();
				if(StringUtils.isEmpty(fileDir)) return null;
				fileDir = fileDir.substring(0,fileDir.lastIndexOf("/"));
				if(fileDir.startsWith("/") && !"/".equals(fileDir)){
					fileDir = fileDir.substring(1);
				}
				ftp.changeWorkingDirectory(fileDir);
	            // 获取ftp上的文件 
				is = ftp.retrieveFileStream(fileName);
				if(is==null) return null;
	            br = new BufferedReader(new InputStreamReader(is));
	            String str;
	            while((str=br.readLine())!=null){
	               sb.append(str);
	            }
	            
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (ftp != null) {
				closeFtp(ftp);
			}
			if(br !=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(is !=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String fileName = "schedule.conf";
		String ftpUrl = "ftp://ftpuser:ftpuser@172.16.199.17/schedule/schedule.conf";
		FTPDownloadInfo ftpInfo = FTPUtils.parseFTPDownloadURL(ftpUrl);
		FtpServerVO ftpvo = new FtpServerVO();
		ftpvo.setHostIp(ftpInfo.getIp());
		ftpvo.setPort(ftpInfo.getPort());
		ftpvo.setUsername(ftpInfo.getAccount());
		ftpvo.setPassword(ftpInfo.getPassword());
//		write(ftpvo, "abc", "schedule", fileName);
		System.out.println(writeFile("aaa",ftpUrl));
		
	}

	/**
	 * 修改ftp文件名称
	 * 
	 * @param ftpIn
	 * @param path
	 *            文件所在目录
	 * @param srcName
	 * @param targetName
	 */
	public static void rename(FtpServerVO ftpIn, String path, String srcName,
			String targetName) {
		FTPClient ftp = openFtp(ftpIn);
		try {
			if (ftp != null) {
				ftp.changeWorkingDirectory(path);
				ftp.rename(srcName, targetName);
			}
		} catch (Exception e) {

		} finally {
			closeFtp(ftp);
		}
	}

	public class FTPDownloadInfo {

		private String ip;
		private int port;
		private String account;
		private String password;
		private String file;
		private long lastAvailableTime = System.currentTimeMillis();

		public long getLastAvailableTime() {
			return lastAvailableTime;
		}

		public void setLastAvailableTime(long lastAvailableTime) {
			this.lastAvailableTime = lastAvailableTime;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		private long activeTime = 0;

		public long getActiveTime() {
			return activeTime;
		}

		public void setActiveTime(long activeTime) {
			this.activeTime = activeTime;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

}
