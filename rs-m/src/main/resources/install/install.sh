#!/bin/sh

Force=$1

TOMCAT_HOME="/opt/hoob/3RD/tomcat7.0.63"
tomcatDir="tomcat"
if [ ! -d "/opt/hoob/3RD/tomcat" ]; then
        tomcatDir="tomcat7.0.63"
fi

TOMCAT_HOME="/opt/hoob/3RD/"$tomcatDir

BASE_PATH="/opt/hoob/NE/rs/rs-m"
BIN_PATH=$BASE_PATH/bin
CONFIG_PATH=$BASE_PATH/etc
INIT_SQL_PATH=$CONFIG_PATH/init_sql

##########INIT Directory##########
function intDirectory()
{	
	if [ ! -d "$1" ];then
		mkdir "$1" -p
		echo "directory $1 not exist,now create it"
	fi		
}

intDirectory $BASE_PATH
intDirectory $BIN_PATH
intDirectory $CONFIG_PATH
intDirectory $INIT_SQL_PATH

##########Copy file from install package to target directory##########

rm -rf $TOMCAT_HOME/webapps/rsm.war
rm -rf $TOMCAT_HOME/webapps/rsm
cp -rf ../rs-m*.war $TOMCAT_HOME/webapps/rsm.war

if [ "$Force" == "-first" ]; then
		cp -rf ../etc/* $CONFIG_PATH
		echo "Copy etc file to $CONFIG_PATH"
		
		cp -rf ../etc/log4j2.xml $CONFIG_PATH/log4j2.xml

		cp -rf ../bin/* $BIN_PATH
		echo "Copy bin file to $BIN_PATH"
		
		cp -rf ../sigar/* $SIGAR_PATH
		echo "Copy bin file to $SIGAR_PATH"
		
        cp -rf ../init_sql/* $INIT_SQL_PATH
        echo "Copy init_sql file to $INIT_SQL_PATH"
		
		chmod +x $BIN_PATH/*.sh
		echo "current epg install in $TOMCAT_HOME/webapps"
		
		

fi




echo "install success..."

##########Copy *.war from install package to tomcat webapps directory##########





exit 0
