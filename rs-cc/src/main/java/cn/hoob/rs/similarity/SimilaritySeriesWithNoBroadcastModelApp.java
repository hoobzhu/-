package cn.hoob.rs.similarity;

import cn.hoob.rs.utils.MySQLUtlis;
import cn.hoob.rs.utils.SysUtils;
import cn.hoob.rs.utils.TfidfUtil;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.spark_project.jetty.util.StringUtil;

import scala.reflect.ClassTag$;
import cn.hoob.rs.model.CategoryAssociation;
import cn.hoob.rs.model.SeriesModel;
import cn.hoob.rs.model.SimiartyDataFeatureModel;
import cn.hoob.rs.model.SimilartyData;
import cn.hoob.rs.model.SimilartyDatas;
import cn.hoob.rs.utils.MySQLUtlis;
import cn.hoob.rs.utils.StringUtils;
import cn.hoob.rs.utils.SysUtils;
import cn.hoob.rs.utils.TfidfUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 合集内容相似性推荐
 * ***/
public class SimilaritySeriesWithNoBroadcastModelApp {
	public static  void main(String[] args) throws Exception {
		//初始化系统参数
		SysUtils.initSysParam(args);
		Builder sparkBuilder =SparkSession.builder().appName("ALSCrossValidationModelApp");
		//本地开发调试时配置成LocalMoodel
		SysUtils.isLocalModel(sparkBuilder);
		//创建 sparkSession
		SparkSession sparkSession= sparkBuilder.getOrCreate();

		//加载数据,加载db里面的全量合集
		Properties properties = new Properties();
		properties.put("user",SysUtils.getSysparamString("epg.jdbc.user"));
		properties.put("password",SysUtils.getSysparamString("epg.jdbc.password"));
		Dataset<Row> seriesRowDataset = sparkSession.read().jdbc(SysUtils.getSysparamString("epg.jdbc.url"),"series", properties);
		Encoder<SeriesModel> seriesModelEncoder = Encoders.bean(SeriesModel.class);
		//合集过滤不满足条件的，转换成简易模型数据
		Dataset<SeriesModel> seriesModelDataset = seriesRowDataset.map(row-> 
		SeriesModel.getSeriesModelWithNotCatgoryids(row),seriesModelEncoder)
		.filter((data) -> data != null);
		//注册合集视图
		seriesModelDataset.createOrReplaceTempView("series");

		//加载数据maping,补全栏目信息
		properties.put("user",SysUtils.getSysparamString("epg.jdbc.user"));
		properties.put("password",SysUtils.getSysparamString("epg.jdbc.password"));
		Dataset<Row> categoryAssociationRowDataset = sparkSession.read().jdbc(SysUtils.getSysparamString("epg.jdbc.url"),"category_association", properties);
		Encoder<CategoryAssociation> categoryAssociationEncoder = Encoders.bean(CategoryAssociation.class);
		//合集过滤不满足条件的，转换成简易模型数据
		Dataset<CategoryAssociation> categoryAssociationDataset = categoryAssociationRowDataset.map(row-> 
		CategoryAssociation.getCategoryAssociationModel(row),categoryAssociationEncoder)
		.filter((data) -> data != null);
		//注册关系视图
		categoryAssociationDataset.createOrReplaceTempView("categoryassociation");

		//关联补全合集数据,这里是栏目id的聚合补全
		Dataset<Row>seriesfullDataset=sparkSession.sql("select first(s.id) as id,first(s.contentId) as contentId,"
				+ "first(s.kind) as kind,first(s.programType) as programType,first(s.cast) as cast,"
				+ "concat_ws('|',collect_set(ca.categoryContentId))as catgoryids from series s "
				+ "left join categoryassociation ca on s.contentId=ca.objectContentId group by s.contentId ");

		//分词拆分后的Dataset,内容类型  安格式封装好
		Dataset<SeriesModel>seriesfullModelDataset=seriesfullDataset.map(row->SeriesModel.getSeriesModel(row),seriesModelEncoder).filter(row->row!=null);

		seriesfullModelDataset.createOrReplaceTempView("seriesfullmodel");
		Dataset<Row>seriesfullRowDataset=sparkSession.sql("select * from seriesfullmodel where similartyText is not null ");

		Dataset<Row> trainningTfidfDataset = TfidfUtil.tfidf(seriesfullRowDataset.filter(row->row.getAs("similartyText")!=null),"similartyText","similartyTexts","\\|");

		//求解合集之间的相似度
		//Dataset<SimilartyDatas> similartyDataset= SysUtils.pickupTheTopSimilarShop(trainningTfidfDataset, trainningTfidfDataset);

		//获取总数
		long count=trainningTfidfDataset.count();
		trainningTfidfDataset.createOrReplaceTempView("trainningTfidfDataset");
		long step=0;
		long nextStep=0;
		Integer pageSize=SysUtils.getSysparamInteger("SimiartyPageSize","2000");
		int rankLimit=SysUtils.getSysparamInteger("recommendSimiartyForAllContent","20");
		while(step<count){
			nextStep=step+pageSize;
			//"cast,catgoryids,contentId,id,kind,programType,similartyText,similartyTexts,rawFeatures,features"
			Dataset<Row> partTrainningTfidfDataset=sparkSession.sql("select * from "
					+ "(select *,row_number() over (partition by contentId order by id desc) as rank from trainningTfidfDataset ) "
					+ " where "+step+"< rank and rank <= "+nextStep+"");
			List<SimilartyDatas> similartyList= SysUtils.pickupTheTopSimilarShop(partTrainningTfidfDataset, trainningTfidfDataset);
			Dataset<SimilartyData> similartys=sparkSession.createDataset(similartyList, Encoders.bean(SimilartyDatas.class))
					.flatMap(row->row.getSimilartyDatas().iterator(), Encoders.bean(SimilartyData.class));
			similartys.createOrReplaceTempView("similartys");
			similartys.show();
			Dataset<Row> similartysFiter=  sparkSession.sql("" +
					" select rs.contentId,concat_ws(',',collect_set(rs.similarContentId)) as contentIds  from ("
					+ "select * from ("
					+ "select contentId,similarContentId,similarty, "
					+ "row_number() over(partition by contentId order by similarty desc) as rn "
					+ "from similartys where contentId!=similarContentId ) "
					+ "tmp where rn<="+rankLimit+") rs group by contentId ");
			MySQLUtlis.excuteBatchReSimilarity(similartysFiter);
			step=nextStep;
		}
	
		sparkSession.stop();
	}
}
