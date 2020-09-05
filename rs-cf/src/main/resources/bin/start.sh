
tomcatServer="tomcat"
if [ ! -d "/opt/hoob/3RD/tomcat" ]; then
        tomcatServer="tomcat7"
fi

result=`service $tomcatServer start`
echo "RS-CF start success"
exit 0

