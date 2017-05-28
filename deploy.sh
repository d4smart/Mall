tomcat_home=/root/docs/apache-tomcat-7.0.73
mall_home=/home/wwwroot/mall.d4smarter.com

echo "===========进入git项目mall目录============="
cd $mall_home


echo "==================git fetch======================"
git fetch

echo "==================git pull======================"
git pull


echo "===========编译并跳过单元测试===================="
mvn clean package -Dmaven.test.skip=true


echo "============删除旧的ROOT.war==================="
rm $tomcat_home/webapps/ROOT.war

echo "======拷贝编译出来的war包到tomcat下-ROOT.war======="
cp $mall_home/target/mall.war  $tomcat_home/webapps/ROOT.war

echo "============删除tomcat下旧的ROOT文件夹============="
rm -rf $tomcat_home/webapps/ROOT


echo "====================关闭tomcat====================="
$tomcat_home/bin/shutdown.sh


echo "================sleep 10s=========================="
for i in {1..10}
do
	echo $i"s"
	sleep 1s
done


echo "====================启动tomcat====================="
$tomcat_home/bin/startup.sh

