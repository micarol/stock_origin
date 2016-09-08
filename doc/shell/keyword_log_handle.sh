#!/bin/sh
## 1. 在各个tomcat 下创建一个以前一天日期命名的临时文件夹(2015-12-16), 将tomcat 中前一天的关键词日志mv到该目录下
## 2. 压缩备份
## 3. 将log 文件mv到unidata 配置好的指定路径, 以便unidata 读取log 将数据入库
## 4. 删除第一步创建的临时文件夹

lstday=`date  +"%Y-%m-%d" -d  "-1 days"`
temp_dir='keyword-'$lstday

echo $lstday ' keyword handle begin' >> ~/scripts/keyword_log_handle_sh.log

mkdir /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/$temp_dir
mv /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/keyword_search.log-$lstday*.txt /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/$temp_dir/
tar -zcvf /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/$temp_dir.tar.gz /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/$temp_dir/
mkdir /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/$temp_dir
mv /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/keyword_search.log-$lstday*.txt /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/$temp_dir/
tar -zcvf /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/$temp_dir.tar.gz /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/$temp_dir/
mkdir /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/$temp_dir
mv /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/keyword_search.log-$lstday*.txt /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/$temp_dir/
tar -zcvf /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/$temp_dir.tar.gz /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/$temp_dir/


cp -r /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/$temp_dir/keyword_search.log-$lstday*.txt /home/uniweibo/keyword_logs/tomcat_3/
rm -r /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_3/$temp_dir
cp -r /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/$temp_dir/keyword_search.log-$lstday*.txt /home/uniweibo/keyword_logs/tomcat_4/
rm -r /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_4/$temp_dir
cp -r /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/$temp_dir/keyword_search.log-$lstday*.txt /home/uniweibo/keyword_logs/tomcat_5/
rm -r /home/uniweibo/tomcat/apache-tomcat-7.0.54_unibizapi_5/$temp_dir

echo $lstday ' keyword handle over' >> ~/scripts/keyword_log_handle_sh.log