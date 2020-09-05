tomcatServer="tomcat"
if [ ! -d "/opt/hoob/3RD/tomcat" ]; then
        tomcatServer="tomcat7"
fi

result=`service $tomcatServer stop`
sleep 3
echo "RS-M is stop"
exit 0

