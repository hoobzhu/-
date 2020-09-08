tomcatDir="tomcat"
if [ ! -d "/opt/fonsview/3RD/tomcat" ]; then
        tomcatDir="tomcat7.0.63"
fi

TITLE='/opt/fonsview/3RD/'$tomcatDir'/webapps/rscf/about.html'
echo `cat $TITLE |grep '<span>'|cut -d '>' -f 2|cut -d '<' -f 1|head -1|tail -1`
