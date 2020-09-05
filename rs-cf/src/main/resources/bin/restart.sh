tomcatServer="tomcat"
if [ ! -d "/opt/hoob/3RD/tomcat" ]; then
        tomcatServer="tomcat7"
fi

result=`service $tomcatServer stop`
sleep 3
result=`service $tomcatServer start`
echo "RS-CF restart success"
exit 0

