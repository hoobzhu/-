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

import java.util.List;
import java.util.Properties;

/**
 * 合集内容相似性推荐
 * ***/
public class SimilaritySeriesModelApp {
	public static  void main(String[] args) throws Exception {
		//初始化系统参数
		SysUtils.initSysParam(args);
		Builder sparkBuilder =SparkSession.builder().appName("SimilaritySeriesModelApp");
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
		SeriesModel.getSeriesModelWithNotCatgoryids(row),seriesModelEncoder);
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
				+ "first(s.updateTime) as updateTime,first(s.status) as status,"
				+ "concat_ws('|',collect_set(ca.categoryContentId))as catgoryids from series s "
				+ "left join categoryassociation ca on s.contentId=ca.objectContentId group by s.contentId ");
		//分词拆分后的Dataset,内容类型  安格式封装好
		Dataset<SeriesModel>seriesfullModelDataset=seriesfullDataset.map(row->SeriesModel.getSeriesModel(row),seriesModelEncoder).filter(row->row!=null);
		//构建StructType
		//List<StructField> fieldList = new ArrayList<>();
		//fieldList.add(DataTypes.createStructField("id", DataTypes.LongType,true));
		//fieldList.add(DataTypes.createStructField("contentId", DataTypes.StringType,true));
		//fieldList.add(DataTypes.createStructField("similartyText", DataTypes.StringType,true));
		// fieldList.add(DataTypes.createStructField("kind", DataTypes.IntegerType,false));
		// fieldList.add(DataTypes.createStructField("cast", DataTypes.IntegerType,false));
		// fieldList.add(DataTypes.createStructField("programType", DataTypes.IntegerType,false));
		// fieldList.add(DataTypes.createStructField("catgoryids", DataTypes.IntegerType,false));
		//StructType rowSchema = DataTypes.createStructType(fieldList);
		//ExpressionEncoder<Row> rowEncoder = RowEncoder.apply(rowSchema);
		//Dataset<Row> seriesfullRowDataset = seriesfullModelDataset.map(
		//		(MapFunction<SeriesModel, Row>) model -> {
		//			List<Object> objectList = new ArrayList<>();
		//			objectList.add(model.getId());
		//			objectList.add(model.getContentId());
		//			objectList.add(model.getSimilartyText());
		//			return RowFactory.create(objectList.toArray());
		//		},
		//		rowEncoder
		//		);
		seriesfullModelDataset.createOrReplaceTempView("seriesfullmodel");
		Dataset<Row>seriesfullRowDataset=sparkSession.sql("select * from seriesfullmodel where similartyText is not null");
		Integer limitNum=SysUtils.getSysparamInteger("orderbyUpdateTimeLimitNum","1000000");
		//按时间排序值推荐前TopN(做分母)
		Dataset<Row>seriesMiniFullRowDataset=sparkSession.sql("select * from seriesfullmodel where status=1 and similartyText is not null order by updateTime desc limit "+limitNum);
		Dataset<Row> trainningTfidfDataset = TfidfUtil.tfidf(seriesMiniFullRowDataset.filter(row->(row.getAs("similartyText")!=null)),"similartyText","similartyTexts","\\|");
		Encoder<SimiartyDataFeatureModel> SimiartyDataFeatureModelEncoder = Encoders.bean(SimiartyDataFeatureModel.class);
		Dataset<SimiartyDataFeatureModel> trainningFeaturedataset = trainningTfidfDataset.map(row->SimiartyDataFeatureModel.getSimiartyDataFeatureModel(row),SimiartyDataFeatureModelEncoder);
		List<SimiartyDataFeatureModel> SimiartyDataFeatureModelList=trainningFeaturedataset.javaRDD().collect();
		SimiartyDataFeatureModel[] SimiartyDataFeatureModelArray = new SimiartyDataFeatureModel[SimiartyDataFeatureModelList.size()];
		SimiartyDataFeatureModelList.toArray(SimiartyDataFeatureModelArray);
		//把加载的与数据广播出去。。。
		final Broadcast<SimiartyDataFeatureModel[]> trainningData = sparkSession.sparkContext().broadcast(SimiartyDataFeatureModelArray, ClassTag$.MODULE$.<SimiartyDataFeatureModel[]>apply(SimiartyDataFeatureModel[].class));
		Dataset<Row> seriesTfidDataset = TfidfUtil.tfidf(seriesfullRowDataset.filter(row->row.getAs("similartyText")!=null),"similartyText","similartyTexts","\\|");
		
		//求解合集之间的相似度
		Dataset<SimilartyDatas> similartyDataset= SysUtils.pickupTheTopSimilarShop(seriesTfidDataset, trainningData);

		JavaRDD<SimilartyData>similartyDataListRdd=similartyDataset.toJavaRDD().map(row->row.getSimilartyDatas()).flatMap(row->row.iterator());
		Dataset<SimilartyData> similartys=sparkSession.createDataset(similartyDataListRdd.rdd(),Encoders.bean(SimilartyData.class));
		similartys.createOrReplaceTempView("similartys");
		
		int rankLimit=SysUtils.getSysparamInteger("recommendSimiartyForAllContent","20");
		Dataset<Row> similartysFiter=  sparkSession.sql("" +
				" select rs.contentId,concat_ws(',',collect_set(rs.similarContentId)) as contentIds  from ("
				+ "select * from ("
				+ "select contentId,similarContentId,similarty, "
				+ "row_number() over(partition by contentId order by similarty desc) as rn "
				+ "from similartys where contentId!=similarContentId ) "
				+ "tmp where rn<="+rankLimit+") rs group by contentId ");
		
		MySQLUtlis.executeSQL("TRUNCATE similar_recommendation",new String[]{});
		//System.out.println(similartysFiter.first());
		//把数据写入mysql
		 similartysFiter.write().format("jdbc")
		.mode(SaveMode.Append)
        .option("url",SysUtils.getSysparamString("rs.jdbc.url"))
        .option("driver", "com.mysql.jdbc.Driver")
        .option("dbtable", "similar_recommendation")
        .option("user", SysUtils.getSysparamString("rs.jdbc.user"))
        .option("password",SysUtils.getSysparamString("rs.jdbc.password"))
        .save();
		 sparkSession.stop();
	}
}
