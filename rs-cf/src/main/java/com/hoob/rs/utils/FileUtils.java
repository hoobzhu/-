package com.hoob.rs.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 与文件相关操作的工具类
 * @author ziven
 *
 */
public class FileUtils {
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);



	public static String readFile(String fileName) {
		File file = new File(fileName);
		StringBuilder sb = new StringBuilder(4096);
		BufferedReader reader = null;
		InputStreamReader read = null;
		String tmp = null;
		try {
			read = new InputStreamReader(new FileInputStream(file), "gbk");
			reader = new BufferedReader(read);
			while ((tmp = reader.readLine()) != null) {
				sb.append(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();
	}

	public static void writeXmlFile(String xmlPath, String fileName,
			Document doc) {
		File programxml = new File(xmlPath + fileName);
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		OutputFormat of = null;
		try {
			fos = new FileOutputStream(programxml);
			osw = new OutputStreamWriter(fos, "UTF-8");
			of = new OutputFormat();
			of.setEncoding("UTF-8");
			of.setIndent(true);
			of.setNewlines(true);
			XMLWriter writer = new XMLWriter(osw, of);
			writer.write(doc);
			writer.close();

		} catch (Exception e) {
			log.error("write xml document failed ：{}", e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (osw != null) {
                    osw.close();
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param srcfilepath
	 * @param dstpath
	 * @param filename
	 * @return 成功复制返回true,否则false
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
			log.error(">>>>>copy file error.{}" , e.getMessage());
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
				log.error(">>>>>close file error.{}" , e.getMessage());
				result = false;
			}
		}
		return result;
	}

	

	/**
	 * 复制文件
	 * 
	 * @throws IOException
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
			log.error(" {}", e);
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
	 * 将内容以某种编码格式写入文件中
	 * 
	 * @param content
	 * @param path
	 *            (包含路径和文件名称)
	 * @param Encode
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String content,String path,String Encode) throws IOException {  
		  File file = new File(path);
		// 如果路径不存在，创建
		  if (!file.getParentFile().exists()) {
			   file.getParentFile().mkdirs();
			  }
		file.createNewFile();// 创建新文件
		  FileOutputStream o = new FileOutputStream(file);;  
		  try {  
		      o.write(content.getBytes(Encode));  
		  } catch (Exception e) {  
			  log.error("write file fail. message :{}",e.getMessage());  
			  return false;
		  }finally{  
			  o.close();
		  }  
		  return true;  
		 }
	
	/** 
     * 根据文件计算出文件的MD5 
     * @param file 
     * @return 
     */  
    public static String getFileMD5(File file) {  
        if (!file.isFile()) {  
            return null;  
        }  
          
        MessageDigest digest = null;  
        FileInputStream in = null;  
        byte buffer[] = new byte[1024];  
        int len;  
        try {  
            digest = MessageDigest.getInstance("MD5");  
            in = new FileInputStream(file);  
            while ((len = in.read(buffer, 0, 1024)) != -1) {  
                digest.update(buffer, 0, len);  
            }  
            in.close();  
  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        BigInteger bigInt = new BigInteger(1, digest.digest());  
  
        return bigInt.toString(16);  
    } 

}
