package cn.hoob.als;


import cn.hoob.model.UrlModel;
import cn.hoob.utils.MySQLUtlis;
import cn.hoob.utils.SysUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.*;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * StreamingContext  wordcount
 * @author zhuqinhe
 */
public class OnlineALSModelByStructuredStreamingApp {
    public static void main(String[] args) throws Exception {


        SysUtils.initSysParam(args);
        SparkConf sparkConf  =new SparkConf().setAppName("OnlineALSModelByStructuredStreamingApp");
        SysUtils.isLocalModel(sparkConf );
        //创建JavaStreamingContext对象
        SparkSession sparkSession= SparkSession.builder().config(sparkConf).getOrCreate();

        //加载已推荐的用户的数据
        Dataset<Row> reuserModeDataset =sparkSession.read().
                format("jdbc").
                option("driver","com.mysql.jdbc.Driver").
                option("user",SysUtils.getSysparamString("oss.jdbc.user")).
                option("password",SysUtils.getSysparamString("oss.jdbc.password")).
                option("url",SysUtils.getSysparamString("oss.jdbc.url")).
                option("dbtable","userprofile").load();
        reuserModeDataset.createOrReplaceTempView("usermodel");

        //加载已推荐的用户的数据
        Dataset<Row> reuserRowDataset =sparkSession.read().
                format("jdbc").
                option("driver","com.mysql.jdbc.Driver").
                option("user",SysUtils.getSysparamString("rs.jdbc.user")).
                option("password",SysUtils.getSysparamString("rs.jdbc.password")).
                option("url",SysUtils.getSysparamString("rs.jdbc.url")).
                option("dbtable","user_recommendation").load();
        reuserRowDataset.createOrReplaceTempView("reUsers");
        //加载已推荐的用户的数据
        Dataset<Row> seriesRowDataset =sparkSession.read().
                format("jdbc").
                option("driver","com.mysql.jdbc.Driver").
                option("user",SysUtils.getSysparamString("epg.jdbc.user")).
                option("password",SysUtils.getSysparamString("epg.jdbc.password")).
                option("url",SysUtils.getSysparamString("epg.jdbc.url")).
                option("dbtable","series").load();
        seriesRowDataset.createOrReplaceTempView("series");


        //加载模型
        String modelSaveUrl=SysUtils.getHDFSRsBaseDir()+"/model/als/";
        ALSModel model=ALSModel.load(modelSaveUrl);

        String filterUrl=SysUtils.getSysparamString("filterUrl","api/v1/playreport");

        //创建DStream
        Dataset<String> kafukaDF= sparkSession
                //读一个流数据，lines其实就是一个输入表
                .readStream()
                //数据源的格式
                .format("kafka")
                .option("kafka.bootstrap.servers", SysUtils.getSysparamString("kafka.brokers"))
                // 也可以订阅多个主题: "topic1,topic2"
                .option("subscribe", SysUtils.getSysparamString("kafka.topics","accesslog_ott"))
                .load()
                //这个算子可以写SQL表达式，写SQL转换类型成string   转成DataSet<String>  类型转换row 转化成string
                .selectExpr("cast(value as string)").as(Encoders.STRING());

        //拆分Kafka topic里面的数据，过滤不符合要求的数据
        Dataset<UrlModel> streamingDF = kafukaDF
                //过滤不匹配的url
                .filter((FilterFunction<String>) line->line.contains(filterUrl))
                //每个url  转化成URLModel
                .map((MapFunction<String, UrlModel>) line->{
                    return UrlModel.getUrlModel(line);
                },Encoders.bean(UrlModel.class))
                .filter((FilterFunction<UrlModel>) line->line!=null);
        streamingDF.createOrReplaceTempView("tmpUsers");

        //获取待推的用户ID
        Dataset<Row> usertIdStreaming=sparkSession.sql("select um.id as uId from tmpUsers tu " +
                " left join reUsers u on u.userId=tu.userId " +
                " left join usermodel um on um.userId=tu.userId " +
                " where u.id is not null ");



         //设置成一个分区，提高输出遍历的性能
        usertIdStreaming.repartition(1);
        StreamingQuery nextResult = usertIdStreaming.writeStream().
                //指定输出到哪
                foreachBatch((urlModelDataset, aLong) -> {
               Dataset<Row> reresult = model.recommendForUserSubset(urlModelDataset, 20);
               reresult.createOrReplaceTempView("userreconmend");
               Dataset<Row>result=sparkSession.sql(""
                    + " select rs.userId as userId,concat_ws(',',collect_set(rs.contentId))as contentIds from ("
                    + " select um.userId,s.contentId from (select uId,explode(recommendations) from userreconmend ) uc "
                    + " left join usermodel um on uc.uId=um.Id "
                    + " left join series s on s.id=uc.col.sId )rs group by rs.userId ");
               MySQLUtlis.excuteBatchReUserneNew(result);
               }).outputMode("update")
                .trigger(Trigger.ProcessingTime(300, TimeUnit.SECONDS))
                .start();
        nextResult.awaitTermination();//阻止当前线程退出
        sparkSession.stop();


    }


}
