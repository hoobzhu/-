package cn.hoob.als;


import cn.hoob.model.UrlModel;
import cn.hoob.utils.MySQLUtlis;
import cn.hoob.utils.SysUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.*;

/**
 * StreamingContext  wordcount
 * @author zhuqinhe
 */
public class OnlineALSModelApp {
    public static void main(String[] args) throws Exception {
        //初始化系统参数
       /* Configuration hadoopConf = new Configuration();
        //hdfs
        hadoopConf.addResource(new Path("/etc/hadoop/conf.cloudera.hdfs/core-site.xml"));
        hadoopConf.addResource(new Path("/etc/hadoop/conf.cloudera.hdfs/hdfs-site.xml"));
        //hbase
        hadoopConf.addResource(new Path("/etc/hbase/conf.cloudera.hbase/hbase-site.xml"));

        String krb5File = "/etc/krb5.conf";
        System.setProperty("java.security.krb5.conf", krb5File);
        String user = "daas/admin@FONSVIEW.COM";
        String keyPath = "/opt/fonsview/NE/daas/etc/keytab/daas.keytab";
        hadoopConf.set("hadoop.security.authentication" , "Kerberos" );
        hadoopConf.set("hadoop.security.authorization", "true");

        UserGroupInformation.setConfiguration(hadoopConf);
        // 认证用户部分
        UserGroupInformation.loginUserFromKeytab(user, keyPath);*/

        SysUtils.initSysParam(args);
        SparkConf sparkConf  =new SparkConf().setAppName("OnlineALSModelApp");
        SysUtils.isLocalModel(sparkConf );
        //创建JavaStreamingContext对象
        // 每隔60秒钟，sparkStreaming作业就会收集最近60秒内的数据源接收过来的数据
        JavaStreamingContext jsc = new JavaStreamingContext(sparkConf , Durations.seconds(180));



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
        reuserRowDataset.createOrReplaceTempView("users");
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
        //创建Kafka参数Map
        Map<String, Object> kafkaParams = new HashMap<>();
        //kafkaParams.put("metadata.broker.list", brokers);旧版本参数
        kafkaParams.put("bootstrap.servers", SysUtils.getSysparamString("kafka.brokers"));
        kafkaParams.put("group.id", "RSSparkStreamingUser");

        //可配置自己的序列化组件
        //kafkaParams.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // latest  当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        // earliest  当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        // none  当各分区下都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常

        kafkaParams.put("auto.offset.reset", "earliest");
        //true 偶然false  控制kafka是否自动提交偏移量
        kafkaParams.put("enable.auto.commit", true);



        String filterUrl=SysUtils.getSysparamString("filterUrl","api/v1/playreport");

        //创建Kafka的topics ，里面可以填多个topic
        Collection<String> topics = Arrays.asList(SysUtils.getSysparamString("kafka.topics","accesslog_ott"));
        //Topic分区
        //Map<TopicPartition, Long> offsets = new HashMap<>();
        //offsets.put(new TopicPartition("topic1", 0), 2L);
        //  JavaInputDStream<ConsumerRecord<Object, Object>> lines = KafkaUtils.createDirectStream(jsc, LocationStrategies.PreferConsistent(),
        //        ConsumerStrategies.Subscribe(topics, kafkaParams,offsets));
        //创建DStream
        JavaInputDStream<ConsumerRecord<Object, Object>> lines = KafkaUtils.createDirectStream(jsc, LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics, kafkaParams));

        //拆分Kafka topic里面的数据，过滤不符合要求的数据
        JavaDStream<UrlModel> linesSplit = lines
                .filter(line->(line.value()+"").contains(filterUrl))
                .flatMap(new FlatMapFunction<ConsumerRecord<Object, Object>, UrlModel>() {
            @Override
            public Iterator<UrlModel> call(ConsumerRecord<Object, Object> line) throws Exception {
                String url=line.value().toString();
                return Arrays.asList(new UrlModel[]{UrlModel.getUrlModel(url)}).iterator();
            }
        }).filter(line->line!=null);
        //实时推荐
        Encoder<UrlModel> urlModelEncoder = Encoders.bean(UrlModel.class);
        linesSplit.foreachRDD(rdd->{

            if(rdd.isEmpty()){
                return;
            }

            Dataset<UrlModel> tmpusers = sparkSession.createDataset(rdd.rdd(), urlModelEncoder);
            tmpusers.createOrReplaceTempView("tmpusers");
            Dataset usertDatset=sparkSession.sql("select u.id as uId from tmpusers tu " +
                    " left join users u on u.userId=tu.userId " +
                    " where u.id !=null ").distinct();
            if(usertDatset.isEmpty()){
                return ;
            }
            Dataset<Row> reresult = model.recommendForUserSubset(usertDatset, 20);
            reresult.show();
            if(reresult.isEmpty()){
                return ;
            }
            reresult.createOrReplaceTempView("userreconmend");
            Dataset<Row>result=sparkSession.sql(""
                    + " select rs.userId as userId,concat_ws(',',collect_set(rs.contentId))as contentIds from ("
                    + " select um.userId,s.contentId from (select uId,explode(recommendations) from userreconmend ) uc "
                    + " left join usermodel um on uc.uId=um.Id "
                    + " left join series s on s.id=uc.col.sId )rs group by rs.userId ");
            if(result.isEmpty()){
                return ;
            }
            result.show();
            MySQLUtlis.excuteBatchReUser(result);
        });
        linesSplit.print();
        jsc.start();
        jsc.awaitTermination();
        jsc.close();

    }


}
