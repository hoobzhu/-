package cn.hoob.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.ml.linalg.BLAS;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession.Builder;

import cn.hoob.model.SimiartyDataFeatureModel;
import cn.hoob.model.SimilartyData;
import cn.hoob.model.SimilartyDatas;


public class SysUtils {
	private static final Logger LOGGER = LogManager.getLogger(SysUtils.class);	
	private static final Properties SYSPARAM = new Properties();
	/**
	 * 加载配置，初始化系统参数
	 * @throws Exception 
	 * */
	public static void initSysParam(String[] args) throws Exception{
		//
		String defaultConfigUrl="/opt/fonsview/NE/rs/rs-cc/etc/config.properties";
		File file = new File(defaultConfigUrl);
		Properties temp = new Properties();
		temp.putAll(SYSPARAM);
		//清除更新前配置
		if(file.exists()){
			InputStream fis = null;
			try {
				fis = new FileInputStream(file);
				SYSPARAM.load(fis);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(),e);
				//恢复
				SYSPARAM.putAll(temp);
			} finally {
				if (null != fis) {
					try {
						fis.close();
					} catch (IOException e) {
						LOGGER.debug(e.getMessage(), e);
					}
				}
			}
		}
		temp.clear();
		temp = null;
		//集群环境运行时加载配置文件
		if(SYSPARAM.isEmpty()){
			SystemProperties  pro=new SystemProperties();
			SYSPARAM.putAll(pro.getSystemProperties());
		}
	}

	/**
	 * 本地开发模式时，加载环境配置
	 * **/
	public static void isLocalModel(Builder sparkBuilder){
		if(StringUtils.isEmpty(SYSPARAM.getProperty("isLocalModel"))||
				!"true".equalsIgnoreCase(SYSPARAM.getProperty("isLocalModel"))){
			return ;
		}
		sparkBuilder.master("local[4]");
		//本地模式时设置使用root用户
		System.setProperty("HADOOP_USER_NAME", "root");
		Configuration conf = new Configuration();
		conf.addResource("/opt/fonsview/NE/rs/rs-cc/etc/core-site.xml");
		conf.addResource("/opt/fonsview/NE/rs/rs-cc/etc/hdfs-site.xml");
		if(StringUtils.isNotEmpty(SYSPARAM.getProperty("hdfsHost"))){
			return ;//指定了链接特定的hdfs，则不加载默认的环境配置
		}
		Iterator<Entry<String, String>> it=conf.iterator();
		while(it.hasNext()){
			Entry<String, String>en=it.next();
			sparkBuilder.config(en.getKey(),en.getValue());
		}	    
	}
	/**
	 * 获取hdfs用户行为日子文件目录
	 * @throws Exception 
	 * **/
	public static String getHDFSUserLogDataUrl() throws Exception{
		String hdfsHost=SysUtils.getSysparamString("hdfsHost");
		String hdfsBaseDir=SysUtils.getSysparamString("hdfsUserLogBaseDir");
		Integer days=SysUtils.getSysparamInteger("days","7");
		//循环拼接需要获取的目录
		StringBuilder url=new StringBuilder();
		for(int i=1;i<=days;i++){
			String turl="";
			if(StringUtils.isNotEmpty(hdfsHost)){
				turl=hdfsHost;
			}
			if(StringUtils.isNotEmpty(hdfsBaseDir)){
				turl=turl+hdfsBaseDir;
			}
			turl=turl+"/"+DateUtils.getLastDay(i, DateUtils.Pattern.yyyyMMdd);
			if(HDFSUtils.pathIsExist(turl)){
				url.append(turl);
				if(i<days){
					url.append(",");	
				}
			}

		}
		return url.toString();
	}
	/**
	 * 获取hdfs内容统计量日子文件目录
	 * @throws Exception 
	 * **/
	public static String getHDFSContentLogDataUrl() throws Exception{
		String hdfsHost=SysUtils.getSysparamString("hdfsHost");
		String hdfsBaseDir=SysUtils.getSysparamString("hdfsContentLogBaseDir");
		Integer days=SysUtils.getSysparamInteger("days","7");
		//循环拼接需要获取的目录
		StringBuilder url=new StringBuilder();
		for(int i=1;i<=days;i++){
			String turl="";
			if(StringUtils.isNotEmpty(hdfsHost)){
				turl=hdfsHost;
			}
			if(StringUtils.isNotEmpty(hdfsBaseDir)){
				turl=turl+hdfsBaseDir;
			}
			turl=turl+"/"+DateUtils.getLastDay(i, DateUtils.Pattern.yyyyMMdd);
			if(HDFSUtils.pathIsExist(turl)){
				url.append(turl);
				if(i<days){
					url.append(",");	
				}
			}

		}
		return url.toString();
	}
	/**
	 * 获取hdfs用户行为日子文件目录
	 * **/
	public static String getHDFSRsBaseDir(){
		String hdfsHost=SysUtils.getSysparamString("hdfsHost");
		String hdfsRsBaseDir=SysUtils.getSysparamString("hdfsRsBaseDir");
		return hdfsHost+"/"+hdfsRsBaseDir;


	}
	public static String getSysparamString(String key,String defaultValue){
		return SYSPARAM.getProperty(key, defaultValue);
	}
	public static Integer getSysparamInteger(String key,String defaultValue){
		String value=SYSPARAM.getProperty(key, defaultValue);
		if(StringUtils.isNotEmpty(value)){
			return Integer.parseInt(value);
		}
		return null;

	}
	public static Boolean getSysparamBoolean(String key,String defaultValue){
		String value=SYSPARAM.getProperty(key, defaultValue);
		if(StringUtils.isNotEmpty(value)){
			return Boolean.parseBoolean(value);
		}
		return false;

	}
	public static String getSysparamString(String key){
		return SYSPARAM.getProperty(key);
	}
	public static Integer getSysparamInteger(String key){
		String value=SYSPARAM.getProperty(key);
		if(StringUtils.isNotEmpty(value)){
			return Integer.parseInt(value);
		}
		return null;

	}
	public static Boolean getSysparamBoolean(String key){
		String value=SYSPARAM.getProperty(key);
		if(StringUtils.isNotEmpty(value)){
			return Boolean.parseBoolean(value);
		}
		return false;

	} 
	public static Boolean sysparamIsEmpty(){
		return SYSPARAM.isEmpty();
	}
	public static void setSysparam(Properties pro){
		SYSPARAM.putAll(pro);
	}
	/**
	 * 数据量小的时候可以通过广播数据来接解决问题，建议20w 一下合集的选用该方法操作
	 * */
	public static Dataset<SimilartyDatas> pickupTheTopSimilarShop(Dataset<Row> tfidDataset,
			Broadcast<SimiartyDataFeatureModel[]> trainningData){
		int pageSize=SysUtils.getSysparamInteger("recommendSimiartyForAllContent","20");
		return tfidDataset.map(new MapFunction<Row, SimilartyDatas>() {
			@Override
			public SimilartyDatas call(Row row) throws Exception {
				SimilartyDatas similartyDatass=new SimilartyDatas();
				List<SimilartyData> similartyDatas = new ArrayList<SimilartyData>();
				String  contentId=row.getAs("contentId");
				Vector features = row.getAs("features");
				SimiartyDataFeatureModel[] trainDataArray = trainningData.value();
				if(ArrayUtils.isEmpty(trainDataArray)){
					return similartyDatass;
				}
				for (SimiartyDataFeatureModel trainData : trainDataArray) {
					SimilartyData similartyData=new SimilartyData();
					Vector trainningFeatures = trainData.getFeatures();
					double dot = BLAS.dot(features.toSparse(), trainningFeatures.toSparse());
					double v1 = Vectors.norm(features.toSparse(), 2.0);
					double v2 = Vectors.norm(trainningFeatures.toSparse(), 2.0);
					double similarty = dot / (v1 * v2);
					//这里这样时计算最相似的一个，不需要先保留每一个的计算只后面才过滤
					similartyData.setContentId(contentId);
					similartyData.setSimilarContentId(trainData.getContentId());
					similartyData.setSimilarty(similarty);
					Collections.sort(similartyDatas,new Comparator<SimilartyData>() {
						@Override
						public int compare(SimilartyData s1, SimilartyData s2) {
							if(s2.getSimilarty()>s1.getSimilarty()){
								return 1;
							}else{
								return -1;
							}
						}
					});
					if(similartyDatas.size()<pageSize){
						similartyDatas.add(similartyData);
					}else{
						if(similartyDatas.get(pageSize-1).getSimilarty()<similarty){
							similartyDatas.remove(pageSize-1);
							similartyDatas.add(similartyData);
						}
					}

				}
				similartyDatass.setContentId(contentId);
				similartyDatass.setSimilartyDatas(similartyDatas);
				return similartyDatass;
			}
		}, Encoders.bean(SimilartyDatas.class));
	}
	/***/
	public static List<SimilartyDatas> pickupTheTopSimilarShop(Dataset<Row> partTrainningTfidfDataset,
			Dataset<Row> fullDataset){
		int pageSize=SysUtils.getSysparamInteger("recommendSimiartyForAllContent","20");
		Iterator<Row> rows=partTrainningTfidfDataset.toLocalIterator();
		List<SimilartyDatas> slist=new ArrayList<SimilartyDatas>();
		while(rows.hasNext()){
			Row row=rows.next();
			SimilartyDatas similartyDatass=new SimilartyDatas();
			List<SimilartyData> similartyDatas = new ArrayList<SimilartyData>();
			String  contentId=row.getAs("contentId");
			Vector features = row.getAs("features");
			fullDataset.foreach(line->{
				SimilartyData similartyData=new SimilartyData();
				Vector trainningFeatures = line.getAs("features");
				String similartycontentId=line.getAs("contentId");
				double dot = BLAS.dot(features.toSparse(), trainningFeatures.toSparse());
				double v1 = Vectors.norm(features.toSparse(), 2.0);
				double v2 = Vectors.norm(trainningFeatures.toSparse(), 2.0);
				double similarty = dot / (v1 * v2);
				//这里这样时计算最相似的一个，不需要先保留每一个的计算只后面才过滤
				similartyData.setContentId(contentId);
				similartyData.setSimilarContentId(similartycontentId);
				similartyData.setSimilarty(similarty);
				Collections.sort(similartyDatas,new Comparator<SimilartyData>() {
					@Override
					public int compare(SimilartyData s1, SimilartyData s2) {
						if(s2.getSimilarty()>s1.getSimilarty()){
							return 1;
						}else{
							return -1;
						}
					}
				});
				if(similartyDatas.size()<pageSize){
					similartyDatas.add(similartyData);
				}else{
					if(similartyDatas.get(pageSize-1).getSimilarty()<similarty){
						similartyDatas.remove(pageSize-1);
						similartyDatas.add(similartyData);
					}
				}
			});
			similartyDatass.setContentId(contentId);
			similartyDatass.setSimilartyDatas(similartyDatas);
			slist.add(similartyDatass);
		}  
		return slist;
	}
	/***/
	public static Dataset<SimilartyData> pickupTheTopSimilarShop(Row row,Dataset<Row> tfidDataset){
		String  contentId=row.getAs("contentId");
		Vector features = row.getAs("features");
		return  tfidDataset.map(line->{
			SimilartyData similartyData=new SimilartyData();
			Vector trainningFeatures = line.getAs("features");
			String similartycontentId=line.getAs("contentId");
			double dot = BLAS.dot(features.toSparse(), trainningFeatures.toSparse());
			double v1 = Vectors.norm(features.toSparse(), 2.0);
			double v2 = Vectors.norm(trainningFeatures.toSparse(), 2.0);
			double similarty = dot / (v1 * v2);
			if (Double.isNaN(similarty) || Double.isInfinite(similarty)) {
				similarty=0;
			}
			//这里这样时计算最相似的一个，不需要先保留每一个的计算只后面才过滤
			similartyData.setContentId(contentId);
			similartyData.setSimilarContentId(similartycontentId);
			similartyData.setSimilarty(similarty);
			return similartyData;
		},Encoders.bean(SimilartyData.class));

	}
	/***/
	private static int[] integerListToArray(List<Integer> integerList){
		int[] intArray = new int[integerList.size()];
		for (int i = 0; i < integerList.size(); i++) {
			intArray[i] = integerList.get(i).intValue();
		}
		return intArray;
	}
	/***/
	private static double[] doubleListToArray(List<Double> doubleList){
		double[] doubleArray = new double[doubleList.size()];
		for (int i = 0; i < doubleList.size(); i++) {
			doubleArray[i] = doubleList.get(i).intValue();
		}
		return doubleArray;
	}
}
