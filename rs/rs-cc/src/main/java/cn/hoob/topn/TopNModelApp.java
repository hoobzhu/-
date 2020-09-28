package cn.hoob.topn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;
import cn.hoob.model.SeriesModel;
import cn.hoob.model.TopNContent;
import cn.hoob.utils.MySQLUtlis;
import cn.hoob.utils.SysUtils;

/**
 *热门内容推荐 
 */

public class TopNModelApp {
	
	private static final Logger LOGGER = LogManager.getLogger(TopNModelApp.class);	
	
	/****/
	public static void main(String[] args) throws Exception {
		//清理原推荐数据

		//初始化系统参数
		SysUtils.initSysParam(args);
		Builder sparkBuilder =SparkSession.builder().appName("TopNModelApp");
		//本地开发调试时配置成LocalMoodel
		SysUtils.isLocalModel(sparkBuilder);
		//创建 sparkSession
		SparkSession sparkSession= sparkBuilder.getOrCreate();
		Dataset<Row> seriesRowDataset = sparkSession.read().
				format("jdbc").
				option("driver","com.mysql.jdbc.Driver").
				option("user",SysUtils.getSysparamString("epg.jdbc.user")).
				option("password",SysUtils.getSysparamString("epg.jdbc.password")).
				option("url",SysUtils.getSysparamString("epg.jdbc.url")).
				option("dbtable","series").load();
				
		Encoder<SeriesModel> seriesModelEncoder = Encoders.bean(SeriesModel.class);
		//合集过滤不满足条件的，转换成简易模型数据
		Dataset<SeriesModel> seriesModelDataset = seriesRowDataset.map((MapFunction<Row, SeriesModel>) row->
		SeriesModel.getSeriesModelWithProgramType(row),seriesModelEncoder);

		//注册视图
		seriesModelDataset.createOrReplaceTempView("series");
		//加载日志数据
		Dataset<String> logDataset= sparkSession.read().textFile(SysUtils.getHDFSContentLogDataUrl().split(","));
		//初始化日志数据
		Encoder<TopNContent> topNContentEncoder = Encoders.bean(TopNContent.class);
		Dataset<TopNContent>topNContentDataset=logDataset.
				map((MapFunction<String, TopNContent>) row->TopNContent.parseModel(row), topNContentEncoder).
				filter((FilterFunction<TopNContent>) row -> row != null);
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
		MySQLUtlis.executeSQL("TRUNCATE topn_recommendation",new String[]{});
		result.write().format("jdbc")
		.mode(SaveMode.Append)
		.option("url",SysUtils.getSysparamString("rs.jdbc.url"))
		.option("driver", "com.mysql.jdbc.Driver")
		.option("dbtable", "topn_recommendation")
		.option("user", SysUtils.getSysparamString("rs.jdbc.user"))
		.option("password",SysUtils.getSysparamString("rs.jdbc.password"))
		.save();
		//result.show();
		sparkSession.stop();


	} 
}
