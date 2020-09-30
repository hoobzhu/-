#!/bin/sh

Force=$1
BASE_PATH="/opt/hoob/NE/rs/rs-cc"
BIN_PATH=$BASE_PATH/bin
BIN_PATH_LIB=$BASE_PATH/lib
CONFIG_PATH=$BASE_PATH/etc
LOG_PATH=$BASE_PATH/log

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
intDirectory $BIN_PATH_LIB
intDirectory $LOG_PATH
##########Copy file from install package to target directory##########

rm -rf $BIN_PATH_LIB/*.jar
cp -rf ../*.jar $BIN_PATH_LIB

if [ "$Force" == "-first" ]; then
		cp -rf ../etc/* $CONFIG_PATH
		echo "Copy etc file to $CONFIG_PATH"
		
		cp -rf ../etc/log4j2.xml $CONFIG_PATH
        echo "Copy log4j2.xml file to $CONFIG_PATH"
        
		cp -rf ../bin/* $BIN_PATH
		echo "Copy bin file to $BIN_PATH"
		
		chmod +x $BIN_PATH/*.sh		
fi

echo "install success..."
exit 0
