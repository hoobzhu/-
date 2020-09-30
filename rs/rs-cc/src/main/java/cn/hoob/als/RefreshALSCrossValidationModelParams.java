package cn.hoob.als;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
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
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SparkSession.Builder;
import cn.hoob.model.Rating;
import cn.hoob.model.SeriesModel;
import cn.hoob.model.UserModel;
import cn.hoob.utils.SysUtils;


/**ALS算法协同过滤推荐算法
 * 使用Spark 2.0 基于Pipeline,ParamMap,CrossValidation
 * 对超参数进行调优，并进行模型选择
 * 
 */

public class RefreshALSCrossValidationModelParams {
	
	private static final Logger LOGGER = LogManager.getLogger(RefreshALSCrossValidationModelParams.class);
	
	/****/
	public static void main(String[] args) throws Exception {
		//清理原推荐数据

		//初始化系统参数
		SysUtils.initSysParam(args);
		Builder sparkBuilder =SparkSession.builder().appName("ALSCrossValidationModelApp");
		//本地开发调试时配置成LocalMoodel
		SysUtils.isLocalModel(sparkBuilder);
		//创建 sparkSession
		SparkSession sparkSession= sparkBuilder.getOrCreate();
		//加载数据,加载db里面的全量合集
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
		SeriesModel.getSeriesModelByIdAndContentId(row),seriesModelEncoder).filter((FilterFunction<SeriesModel>) (data) -> data != null);
		//注册视图
		seriesModelDataset.createOrReplaceTempView("series");
		//加载日子数据
		Dataset<String> logDataset= sparkSession.read().textFile(SysUtils.getHDFSUserLogDataUrl().split(","));
		//初始化日志数据
		Encoder<Rating> ratingEncoder = Encoders.bean(Rating.class);
		Dataset<Rating>ratingSimpleDataset=logDataset.map((MapFunction<String, Rating>) row->Rating.parseSimpleRating(row),
				ratingEncoder).filter((FilterFunction<Rating>) rating -> rating != null);
		//ratingSimpleDataset.show();
		//打捞用户Id,更新db用户数据
		ratingSimpleDataset.createOrReplaceTempView("ratingsimple");

		//直接加载AAA db用户表获取用户的全量信息，无需自己打捞构建
		//Dataset<Row> userIdDataset=sparkSession.sql("select DISTINCT userId from ratingsimple ");
		//userIdDataset.write().format("jdbc")
		//.mode(SaveMode.Append)
		//.option("url",SysUtils.getSysparamString("rs.jdbc.url"))
		//.option("driver", "com.mysql.jdbc.Driver")
		//.option("dbtable", "reuser")
		//.option("user", SysUtils.getSysparamString("rs.jdbc.user"))
		//.option("password",SysUtils.getSysparamString("rs.jdbc.password"))
		//.save();
		//properties.put("user",SysUtils.getSysparamString("rs.jdbc.user"));
		//properties.put("password",SysUtils.getSysparamString("rs.jdbc.password"));
		//Dataset<Row> reuserRowDataset = sparkSession.read().jdbc(SysUtils.getSysparamString("rs.jdbc.url"),"reuser", properties);
		Dataset<Row> reuserRowDataset = sparkSession.read().
				format("jdbc").
				option("driver","com.mysql.jdbc.Driver").
				option("user",SysUtils.getSysparamString("oss.jdbc.user")).
				option("password",SysUtils.getSysparamString("oss.jdbc.password")).
				option("url",SysUtils.getSysparamString("oss.jdbc.url")).
				option("dbtable","userprofile").load();
		Encoder<UserModel> userModelEncoder = Encoders.bean(UserModel.class);
		Dataset<UserModel>userModelDataset=reuserRowDataset.map((MapFunction<Row, UserModel>) row->UserModel.getUserModel(row), userModelEncoder);
		userModelDataset.createOrReplaceTempView("usermodel");
		//关联用户，内容表 补全日子数据，构建完整的rating对象
		Dataset<Row>ratingfullDataset=sparkSession.sql("select user.id as uId,s.id as sId,rslog.rating as rating "
				+ " from ratingsimple rslog "
				+ " left join series s on rslog.scontentId=s.contentId"
				+ " left join usermodel user on rslog.userId=user.userId "
				+ " where user.id!=null and s.id!=null ");
		//将整个数据集划分为训练集和测试集
		//注意training集将用于Cross Validation,而test集将用于最终模型的评估
		//在traning集中，在Croos Validation时将进一步划分为K份，每次留一份作为
		//Validation，注意区分：ratings.randomSplit（）分出的Test集和K 折留
		//下验证的那一份完全不是一个概念，也起着完全不同的作用，一定不要相混淆
		Dataset<Row>[] splits = ratingfullDataset.randomSplit(new double[]{0.8, 0.2});
		Dataset<Row> training = splits[0];
		Dataset<Row> test = splits[1];

		// Build the recommendation model using ALS on the training data
		ALS als=new ALS()
		.setMaxIter(8)
		.setRank(20).setRegParam(0.8)
		.setUserCol("uId")
		.setItemCol("sId")
		.setRatingCol("rating")
		.setPredictionCol("predict_rating");
		/*
		 * (1)秩Rank：模型中隐含因子的个数：低阶近似矩阵中隐含特在个数，因子一般多一点比较好，
		 * 但是会增大内存的开销。因此常在训练效果和系统开销之间进行权衡，通常取值在10-200之间。
		 * (2)最大迭代次数：运行时的迭代次数，ALS可以做到每次迭代都可以降低评级矩阵的重建误差，
		 * 一般少数次迭代便能收敛到一个比较合理的好模型。
		 * 大部分情况下没有必要进行太对多次迭代（10次左右一般就挺好了）
		 * (3)正则化参数regParam：和其他机器学习算法一样，控制模型的过拟合情况。
		 * 该值与数据大小，特征，系数程度有关。此参数正是交叉验证需要验证的参数之一。
		 */
		ALSModel model = als.fit(training);


		/*
		 * (1)秩Rank：模型中隐含因子的个数：低阶近似矩阵中隐含特在个数，因子一般多一点比较好，
		 * 但是会增大内存的开销。因此常在训练效果和系统开销之间进行权衡，通常取值在10-200之间。
		 * (2)最大迭代次数：运行时的迭代次数，ALS可以做到每次迭代都可以降低评级矩阵的重建误差，
		 * 一般少数次迭代便能收敛到一个比较合理的好模型。
		 * 大部分情况下没有必要进行太对多次迭代（10次左右一般就挺好了）
		 * (3)正则化参数regParam：和其他机器学习算法一样，控制模型的过拟合情况。
		 * 该值与数据大小，特征，系数程度有关。此参数正是交叉验证需要验证的参数之一。
		 */
		// Configure an ML pipeline, which consists of one stage
		//一般会包含多个stages
		Pipeline pipeline=new Pipeline().
				setStages(new PipelineStage[] {als});
		// We use a ParamGridBuilder to construct a grid of parameters to search over.
		ParamMap[] paramGrid=new ParamGridBuilder()
				.addGrid(als.rank(),new int[]{5,8,10,12,15,20})
				.addGrid(als.regParam(),new double[]{0.05,0.10,0.15,0.20,0.40,0.75,0.80})
				.addGrid(als.maxIter(),new int[]{1,3,5,8,10,12,15,20})
				.build();

		// CrossValidator 需要一个Estimator,一组Estimator ParamMaps, 和一个Evaluator.
		// （1）Pipeline作为Estimator;
		// （2）定义一个RegressionEvaluator作为Evaluator，并将评估标准设置为“rmse”均方根误差
		// （3）设置ParamMap
		// （4）设置numFolds
		CrossValidator cv=new CrossValidator()
				.setEstimator(pipeline)
				.setEvaluator(new RegressionEvaluator()
						.setLabelCol("rating")
						.setPredictionCol("predict_rating")
						.setMetricName("rmse"))
				.setEstimatorParamMaps(paramGrid)
				.setNumFolds(5);

		// 运行交叉检验，自动选择最佳的参数组合
		CrossValidatorModel cvModel=cv.fit(training);
		//保存模型
		//cvModel.save("/home/hadoop/spark/cvModel_als.modle");

		System.out.println("numFolds: "+cvModel.getNumFolds());
		//Test数据集上结果评估
		Dataset<Row> predictions=cvModel.transform(test);
		RegressionEvaluator evaluator = new RegressionEvaluator()
				.setMetricName("rmse")//RMS Error
				.setLabelCol("rating")
				.setPredictionCol("predict_rating");
		Double rmse = evaluator.evaluate(predictions);
		System.out.println("RMSE @ test dataset " + rmse);
		//Output: RMSE @ test dataset 0.943644792277118

		Pipeline bestPipeline = (Pipeline) cvModel.bestModel().parent();
		PipelineStage[] stages = bestPipeline.getStages();
		//输出最佳参数存储于db,模型设置成最优参数
		PipelineStage stage=stages[0];
		int rank= (int) stage.extractParamMap().get(stage.getParam("rank")).get();
		double regParam= (double) stage.extractParamMap().get(stage.getParam("regParam")).get();
		int maxIter= (int) stage.extractParamMap().get(stage.getParam("maxIter")).get();
		System.out.println("rank:"+rank+",regParam:"+regParam+",maxIter:"+maxIter);

		sparkSession.stop();

	} 
	
}
