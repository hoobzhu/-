#!/bin/sh


yarn_application_id=`yarn --config /etc/hadoop/conf.cloudera.yarn application --list | grep "RS.TopNModelApp*" | awk -F ' ' '{print $1}'`
if [[ -z $yarn_application_id ]];
then
	echo "spark stopped."
else
	for var in ${yarn_application_id[@]}
	do
		yarn --config /etc/hadoop/conf.cloudera.yarn application --kill $var
	done
echo "spark stopped"
fi