package cn.hoob.rs.topn;

import cn.hoob.rs.utils.MySQLUtlis;
import cn.hoob.rs.utils.SysUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.param.ParamMap;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.ml.tuning.CrossValidator;
import org.apache.spark.ml.tuning.CrossValidatorModel;
import org.apache.spark.ml.tuning.ParamGridBuilder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;
import org.apache.spark.sql.types.DataTypes;

import cn.hoob.rs.model.SeriesModel;
import cn.hoob.rs.model.TopNContent;

import java.util.Properties;
/**
 *热门内容推荐 
 */

public class TopNModelApp {
	
	private static final Logger logger = LogManager.getLogger(TopNModelApp.class);	
	
	
	public static void main(String[] args) throws Exception {
		//清理原推荐数据
		
        //初始化系统参数
		SysUtils.initSysParam(args);
		Builder sparkBuilder =SparkSession.builder().appName("TopNModelApp");
		//本地开发调试时配置成LocalMoodel
		SysUtils.isLocalModel(sparkBuilder);
		//创建 sparkSession
		SparkSession sparkSession= sparkBuilder.getOrCreate();
	    
		
		Properties properties = new Properties();
		properties.put("user",SysUtils.getSysparamString("epg.jdbc.user"));
		properties.put("password",SysUtils.getSysparamString("epg.jdbc.password"));
		Dataset<Row> seriesRowDataset = sparkSession.read().jdbc(SysUtils.getSysparamString("epg.jdbc.url"),"series", properties);
		Encoder<SeriesModel> seriesModelEncoder = Encoders.bean(SeriesModel.class);
		//合集过滤不满足条件的，转换成简易模型数据
		Dataset<SeriesModel> seriesModelDataset = seriesRowDataset.map(row-> 
			  SeriesModel.getSeriesModelWithProgramType(row),seriesModelEncoder);
		
        //注册视图
		seriesModelDataset.createOrReplaceTempView("series");
		//加载日志数据
		Dataset<String> logDataset= sparkSession.read().textFile(SysUtils.getHDFSContentLogDataUrl().split(","));
		//初始化日志数据
		Encoder<TopNContent> topNContentEncoder = Encoders.bean(TopNContent.class);
		Dataset<TopNContent>topNContentDataset=logDataset.map(row->TopNContent.parseModel(row), topNContentEncoder).filter(row -> row != null);
		topNContentDataset.createOrReplaceTempView("topncontent");
		Integer limitNum=SysUtils.getSysparamInteger("topNLimitNum","20");
		Dataset<Row> result=sparkSession.sql(""
				+ " select contentId,playCount,programType from  (select s.contentId,tp.playCount,s.programType,"
				+ " row_number() over(partition by s.programType order by playCount desc) as rn "
				+ " from topncontent tp left join series s on tp.contentId =s.contentId where s.status=1)"
				+ " where rn<="+limitNum)
				//.distinct();
				.dropDuplicates("contentId");
		MySQLUtlis.excuteBatchReTopN(result);
		//MySQLUtlis.executeSQL("TRUNCATE topn_recommendation",new String[]{});
		//result.write().format("jdbc")
       //.mode(SaveMode.Append)
       //.option("url",SysUtils.getSysparamString("rs.jdbc.url"))
       //.option("driver", "com.mysql.jdbc.Driver")
       //.option("dbtable", "topn_recommendation")
       //.option("user", SysUtils.getSysparamString("rs.jdbc.user"))
       //.option("password",SysUtils.getSysparamString("rs.jdbc.password"))
       //.save();
         sparkSession.stop();

		
	} 
	
}
