package cn.hoob.rs.utils;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HDFSUtils {
	static final Logger log = LogManager.getLogger(HDFSUtils.class);

	/*
	 * 获取FileSystem  对象
	 * **/
	public static FileSystem getFileSystem() throws Exception{
		Configuration conf = new Configuration();

		if(StringUtils.isEmpty(SysUtils.getSysparamString("isLocalModel"))||
				!"true".equalsIgnoreCase(SysUtils.getSysparamString("isLocalModel"))){
			return  FileSystem.newInstance(conf);
		}else{
			conf.addResource("/opt/hoob/NE/rs/rs-cc/etc/core-site.xml");
			conf.addResource("/opt/hoob/NE/rs/rs-cc/etc/hdfs-site.xml");
			if(StringUtils.isNotEmpty(SysUtils.getSysparamString("hdfsHost"))){
				//指定了链接特定的hdfs，则不加载默认的环境配置
				return 	FileSystem.newInstance(new URI(SysUtils.getSysparamString("hdfsHost")),conf,"root");
			}else{
				return  FileSystem.newInstance(conf);
			}
		}

	}

	/**
	 * 从HDFS中下载文件到客户端本地磁盘
	 * @throws Exception 
	 */

	public static void get(String serverUri,String localUri) throws Exception{

		getFileSystem().copyToLocalFile(new Path(serverUri), new Path(localUri));

		getFileSystem().close();

	}
	/**
	 * 文件或目录重名名,参数都是全路径
	 * @throws Exception 
	 */
	public  static void rename(String oldName,String newName) throws Exception{

		getFileSystem().rename(new Path(oldName), new Path(newName));

		getFileSystem().close();

	}
	/**
	 * 新建目录
	 * @throws Exception 
	 */
	public  static void mkdir(String path) throws Exception{

		getFileSystem().mkdirs(new Path(path));

		getFileSystem().close();
	}
	/**
	 * 删除文件
	 * @throws Exception 
	 */
	public static void rm(String path) throws Exception{

		getFileSystem().deleteOnExit(new Path(path));
		getFileSystem().close();
	}
	/**
	 * 判断路劲是否存在
	 * @throws Exception 
	 */
	public static boolean pathIsExist(String path) throws  Exception{
	  boolean flag=getFileSystem().exists(new Path(path));
	  getFileSystem().close();
	  return flag;
	}
	/**
	 * 判断是否是目录
	 * @throws Exception 
	 */
	public static boolean isDirectory(String path)throws Exception{
		boolean flag=getFileSystem().isDirectory(new Path(path));
		  getFileSystem().close();
		  return flag;
	}
	/**
	 * 判断是否是目录
	 * @throws Exception 
	 */
	public static boolean isFile(String path)throws Exception{
		boolean flag=getFileSystem().isFile(new Path(path));
		  getFileSystem().close();
		  return flag;
	}
}
