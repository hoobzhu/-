#!/bin/sh

SPARKAPP_HOME="/opt/hoob/NE/rs/rs-cc"
APP_NAME=RS.OnlineALSModelApp
APP_JAR_NAME=rs.jar
APP_HOME=$SPARKAPP_HOME/lib
APP_CONF_PATH=$SPARKAPP_HOME/etc
export CLASSPATH=$JRE_PATH/lib/:$CLASSPATH
export PATH=$JRE_PATH/bin:$PATH
chmod 755 $JRE_PATH/bin/java
export HADOOP_HOME=/opt/cloudera/parcels/CDH
export PATH=$HADOOP_HOME/bin:$PATH
export SPARK_KAFKA_VERSION=0.10


base_dir=$(dirname $0)

ES_ENV_FILE=$base_dir/env.sh
if [ -f "$ES_ENV_FILE" ]; then
    . "$ES_ENV_FILE"
fi
command="spark2-submit \
 			 --master yarn \
 			 --deploy-mode cluster \
 			 --name $APP_NAME \
 			 --files $hdfs_conf_path/hdfs-site.xml,$hdfs_conf_path/core-site.xml,$APP_CONF_PATH/config.properties \
 			 --num-executors 3 \
 			 --executor-cores 6 \
 			 --executor-memory 8g \
 			 --driver-memory 8g \
 			 --driver-class-path  $APP_HOME/mysql-connector-java-8.0.17.jar \
       --jars $APP_HOME/mysql-connector-java-8.0.17.jar \
 			 --principal daas/admin@hoob.COM \
 			 --keytab /opt/hoob/NE/daas/etc/keytab/daas.keytab \
 			 --class cn.hoob.rscc.als.OnlineALSModelApp $APP_HOME/$APP_JAR_NAME "
start(){
    exec $command &>$SPARKAPP_HOME/log/rs.log &
    echo "start success!"
}
start
