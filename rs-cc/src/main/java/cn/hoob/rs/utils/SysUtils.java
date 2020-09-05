package cn.hoob.rs.utils;

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
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;

import cn.hoob.rs.model.SimiartyDataFeatureModel;
import cn.hoob.rs.model.SimilartyData;
import cn.hoob.rs.model.SimilartyDatas;


public class SysUtils {
	private static final Logger logger = LogManager.getLogger(SysUtils.class);	
	private static final Properties sysparam = new Properties();
	/**
	 * 加载配置，初始化系统参数
	 * */
	public static void initSysParam(String[] args){
		//
		String defaultConfigUrl="/opt/hoob/NE/rs/rs-cc/etc/config.properties";
		if(args!=null&&args.length>0){
			//指定了配置文件则以配置指定的文件初始化系统
			defaultConfigUrl=args[0];
		}
		File file = new File(defaultConfigUrl);
		Properties temp = new Properties();
		temp.putAll(sysparam);
		//清除更新前配置
		if(file.exists()){
			InputStream fis = null;
			try {
				fis = new FileInputStream(file);
				sysparam.load(fis);
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
				//恢复
				sysparam.putAll(temp);
			} finally {
				if (null != fis) {
					try {
						fis.close();
					} catch (IOException e) {
						logger.debug(e.getMessage(), e);
					}
				}
			}
		}
		temp.clear();
		temp = null;
	}
	/**
	 * 本地开发模式时，加载环境配置
	 * **/
	public static void isLocalModel(Builder sparkBuilder){
		if(StringUtils.isEmpty(sysparam.getProperty("isLocalModel"))||
				!"true".equalsIgnoreCase(sysparam.getProperty("isLocalModel"))){
			return ;
		}
		sparkBuilder.master("local[4]");
		//本地模式时设置使用root用户
		System.setProperty("HADOOP_USER_NAME", "root");
		Configuration conf = new Configuration();
		conf.addResource("/opt/hoob/NE/rs/rs-cc/etc/core-site.xml");
		conf.addResource("/opt/hoob/NE/rs/rs-cc/etc/hdfs-site.xml");
		if(StringUtils.isNotEmpty(sysparam.getProperty("hdfsHost"))){
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
		return sysparam.getProperty(key, defaultValue);
	}
	public static Integer getSysparamInteger(String key,String defaultValue){
		String value=sysparam.getProperty(key, defaultValue);
		if(StringUtils.isNotEmpty(value)){
			return Integer.parseInt(value);
		}
		return null;

	}
	public static Boolean getSysparamBoolean(String key,String defaultValue){
		String value=sysparam.getProperty(key, defaultValue);
		if(StringUtils.isNotEmpty(value)){
			return Boolean.parseBoolean(value);
		}
		return false;

	}
	public static String getSysparamString(String key){
		return sysparam.getProperty(key);
	}
	public static Integer getSysparamInteger(String key){
		String value=sysparam.getProperty(key);
		if(StringUtils.isNotEmpty(value)){
			return Integer.parseInt(value);
		}
		return null;

	}
	public static Boolean getSysparamBoolean(String key){
		String value=sysparam.getProperty(key);
		if(StringUtils.isNotEmpty(value)){
			return Boolean.parseBoolean(value);
		}
		return false;

	}
	/**
	 * 数据量小的时候可以通过广播数据来接解决问题，建议20w 一下合集的选用该方法操作
	 * */
	public static Dataset<SimilartyDatas> pickupTheTopSimilarShop(Dataset<Row> TfidDataset, Broadcast<SimiartyDataFeatureModel[]> trainningData){
        int pageSize=SysUtils.getSysparamInteger("recommendSimiartyForAllContent","20");
		return TfidDataset.map(new MapFunction<Row, SimilartyDatas>() {
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
	
	public static List<SimilartyDatas> pickupTheTopSimilarShop(Dataset<Row> partTrainningTfidfDataset, Dataset<Row> fullDataset){
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
	
	public static Dataset<SimilartyData> pickupTheTopSimilarShop(Row row,Dataset<Row> TfidDataset){
                String  contentId=row.getAs("contentId");
                Vector features = row.getAs("features");
                return  TfidDataset.map(line->{
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
    private static int[] integerListToArray(List<Integer> integerList){
        int[] intArray = new int[integerList.size()];
        for (int i = 0; i < integerList.size(); i++) {
            intArray[i] = integerList.get(i).intValue();
        }
        return intArray;
    }

    private static double[] doubleListToArray(List<Double> doubleList){
        double[] doubleArray = new double[doubleList.size()];
        for (int i = 0; i < doubleList.size(); i++) {
            doubleArray[i] = doubleList.get(i).intValue();
        }
        return doubleArray;
    }
}
