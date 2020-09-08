package com.hoob.rs.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.hoob.rs.sys.vo.FileProviderVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.sl.usermodel.PictureData.PictureType;

import com.hoob.rs.sys.vo.FileProviderVO;

public class FileUtil {
	static Logger LOG = LogManager.getLogger(FileUtil.class);
	// 连接超时时间，默认10秒，可以在配置文件中配置
	private static int connectTimeout = 10000;
	static {
		try {
			connectTimeout = 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 删除临时图片
	 * @param picMap
	 */
	private static void deleteTempPic(Map<String, String> picMap,String localPath){
		Iterator<String> it = picMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = picMap.get(key);

			if (StringUtils.isNotEmpty(value)) {
				String[] str = value.split(",");
				for (int i = 0; i < str.length; i++) {
					File f = new File(localPath+str[i]);
					if (f.exists()) {
						f.delete();
					}
				}
			}
		}
	}


	/**
	 * http方式下载文件
	 * 
	 * @param httpUrl
	 *            文件的http路径
	 * @param file
	 *            本地文件
	 * @return
	 */
	public static boolean httpDownload(String httpUrl, File file) {
		LOG.info("Start to download file:{}" , httpUrl);
		if(StringUtils.isEmpty(httpUrl) || file==null){ 
			return false;
		}
		int byteread = 0;
		URL url = null;
		URLConnection urlConn = null;
		InputStream is = null;
		FileOutputStream os = null;
		try {
			url = new URL(httpUrl);
			urlConn = url.openConnection();
			urlConn.setConnectTimeout(connectTimeout);
			urlConn.setReadTimeout(connectTimeout);
			is = urlConn.getInputStream();
			String path = file.getAbsolutePath();
			File f = new File(path.substring(0,path.lastIndexOf(File.separator)));
			if(!f.exists()){
				f.mkdirs();
			}	
			os = new FileOutputStream(file);

			byte[] buffer = new byte[1204];
			while ((byteread = is.read(buffer)) != -1) {
				os.write(buffer, 0, byteread);
			}
			LOG.info("Succeed to download file:{}" , httpUrl , ", localpath:{}"
					, file.getAbsolutePath());
			return true;
		} catch (MalformedURLException e1) {
			LOG.error("Failed to download file:{}" , httpUrl , ", localpath:{}"
					, file.getAbsolutePath());
			e1.printStackTrace();
			LOG.error(" {}", e1);
			return false;
		} catch (IOException e) {
			LOG.error("Failed to download file:{}" , httpUrl , ", localpath:{}"
					, file.getAbsolutePath());
			e.printStackTrace();
			LOG.error(" {}", e);
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error(" {}", e);
			}
		}
	}

	/**
	 * copy file
	 * 
	 * @param srcfilepath
	 * @param dstpath
	 * @param filename
	 */
	public static boolean copyFile(String srcfilepath, String dstpath,
			String filename) {
		boolean result = true;
		File srcFile = new File(srcfilepath);
		File dstPathFile = new File(dstpath);
		if (!dstPathFile.exists()) {
			dstPathFile.mkdirs();
		}
		File dstFile = new File(dstpath + "/" + filename);
		FileInputStream fis = null;

		FileOutputStream fos = null;
		int byteread = 0;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(dstFile);
			byte[] buffer = new byte[1204];
			while ((byteread = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, byteread);
			}

		} catch (Exception e) {
			LOG.error(">>>>>copy file error.{}" , e.getMessage());
			result = false;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				LOG.error(">>>>>close file error.{}" , e.getMessage());
				result = false;
			}
		}
		return result;
	}



	/**
	 * 图片格式转换
	 * @param source 源图片路径
	 * @param formatName 将要转换的图片格式
	 * @param result 目标图片路径
	 */
	public static void convert(String source, String formatName, String result) {
		try {
			File f = new File(source); 
			f.canRead(); 
			BufferedImage src = ImageIO.read(f); 
			ImageIO.write(src, formatName, new File(result)); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
	}

	/**
	 * 根据图片名称得到后缀
	 * @param picUrl
	 * @return 后缀名(如：.jpg )
	 */
	public static String getPicSuffix(String picUrl) {
		// 默认.jpg
		String suffix = ".jpg";
		if (StringUtils.isNotEmpty(picUrl)) {
			// picUrl是一个图片序列
			String[] picArray = picUrl.split(",");
			// 取第一个图片的后缀名
			// 添加图片为空的判断
			if (picArray.length > 0) {
				for (int i = 0; i < picArray.length; i++) {
					String firstPicUrl = picArray[i];
					if (!com.hoob.rs.utils.StringUtils.paramIsNull(firstPicUrl)
							&& firstPicUrl.indexOf(".")!=-1) {
						suffix = firstPicUrl.substring(firstPicUrl.lastIndexOf("."), firstPicUrl.length());
						break;
					}
				}
			}
		}
		return suffix;
	}

	/**
	 * 读取指定目录下的所有文件和文件夹
	 * @param path
	 * @param regex
	 * @return
	 */
	public static List<FileProviderVO> getListFiles(String path, String regex) {
		String encoding = System.getProperty("file.encoding");  
		LOG.debug(">>>>>system encoding:"+encoding);
		List<FileProviderVO> allFiles = new ArrayList<FileProviderVO>();
		if(path==null || "".equals(path)){
			return allFiles;
		}
		File file = new File(path);
		Pattern pa = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		if(file.isDirectory()){
			File[] f = file.listFiles();
			if (null != f) {
				for (int i = 0; i < f.length; i++) {
					Matcher ma = pa.matcher(f[i].getName());
					if (!f[i].isDirectory() && !ma.matches()) {
						continue;
					}
					FileProviderVO fp = new FileProviderVO();
					fp.setName(f[i].getName());
					fp.setPath(f[i].getPath());
					fp.setUrl(f[i].getPath());
					fp.setLength(f[i].length() + "");
					if (f[i].getName().contains(".apk")) {
						fp.setUrl(fp.getPath());
					}
					if (f[i].isDirectory()) {
						fp.setType(0);
					} else {
						fp.setType(1);
					}
					allFiles.add(fp);
				}
			}
		}else {
			Matcher ma = pa.matcher(file.getName());
			if(!ma.matches()){
				return allFiles;
			}
			FileProviderVO fp = new FileProviderVO();
			fp.setName(file.getName());
			fp.setPath(file.getPath());
			fp.setUrl(file.getPath());
			fp.setType(1);
			if (file.getName().contains(".apk")) {
				fp.setUrl(fp.getPath());
			}
			allFiles.add(fp);
		}
		return allFiles;
	}

	/**
	 * 处理九种图片信息
	 * @param oldPath
	 * @param mediaType
	 * @param timeStamp
	 * @param contentId
	 * @param cpId
	 * @param localPath
	 * @param oldTimeStamp
	 * @return
	 */
	public static String processNinePicInfo(String oldPath,int mediaType,
			String contentId, String cpId,String localPath,String oldTimeStamp) {
		if(StringUtils.isEmpty(localPath) || StringUtils.isEmpty(oldPath)) {
			return null;
		}
		String timeStamp = String.valueOf(System.currentTimeMillis());// 时间戳
		String newDir = localPath+"picture/" + cpId + "/" + mediaType + "/" + 
				DateUtils.formatDate(new Date(), DateUtils.Pattern.yyyyMMdd)
				+ "/" + contentId + "/";
		String suffix = oldPath.substring(oldPath.lastIndexOf("."),oldPath.length());
		//http://localhost:8080  /cms_images/xxx.jpg
		oldPath = localPath+oldPath;
		String newPath = newDir + timeStamp + suffix;
		if (!oldPath.equals(newPath)) {
			// 将上传的图片复制成带分辨率的图片地址（这里不转换，上传什么就保存什么，将oldPath的图片，根据分辨率复制成目标文件）
			copyFile(oldPath, newDir, timeStamp + suffix);
			File f = new File(oldPath);
			if (f.exists() && f.getName().endsWith(".tmp")) {
				f.delete();
			}
		}
		if(!StringUtils.isEmpty(oldTimeStamp)){
			// 获取文件夹下面的文件
			List<FileProviderVO> fileList = new ArrayList<FileProviderVO>();
			fileList = getListFiles(newDir,".*");
			for (FileProviderVO f : fileList) {
				if (f.getName().indexOf(oldTimeStamp) != -1) {
					File file = new File(f.getPath());
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
		return newPath.replace(localPath, "");
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 */
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File targetFile) {
		FileChannel in = null;
		FileChannel out = null;
		if (!targetFile.exists()) {
			try {
				targetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			in = new FileInputStream(sourceFile).getChannel();
			out = new FileOutputStream(targetFile).getChannel();
			in.transferTo(0, in.size(), out);
		} catch (Exception e) {
			LOG.error(" {}", e);
		} finally {
			// 关闭流
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {

				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {

				}
			}
		}

	}

	/**
	 * @Author vivi 2015-7-2 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (null != children && children.length > 0) {
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}



	/**
	 * 处理非本地服务器图片地址
	 * @param url 
	 * @param localPath 本地服务器根目录
	 * @return
	 */
	private static String convertUrl(String url,String localPath) {
		if(!url.toLowerCase().startsWith("ftp") && !url.toLowerCase().startsWith("http")) {
			return url;
		}
		String localDirStr = localPath+DateUtils.formatDate(new Date(), DateUtils.Pattern.yyyy_MM_dd)+"/";
		File localDir = new File(localDirStr);
		if (!localDir.exists()) {
			localDir.mkdirs();
		}
		String newUrl = localDirStr+System.currentTimeMillis()+url.substring(url.lastIndexOf("."), url.length());
		try {
			if (url.toLowerCase().startsWith("ftp")) {
				if(!FTPUtils.downloadFile(url, newUrl)){
					newUrl="";
				}

			} else if (url.toLowerCase().startsWith("http")) {
				if(!httpDownload(url, new File(newUrl))){
					newUrl="";
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return newUrl.replace(localPath, "");
	}

}
