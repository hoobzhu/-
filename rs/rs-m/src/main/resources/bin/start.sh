
tomcatServer="tomcat"
if [ ! -d "/opt/fonsview/3RD/tomcat" ]; then
        tomcatServer="tomcat7"
fi

result=`service $tomcatServer start`
echo "RS-M start success"
exit 0

