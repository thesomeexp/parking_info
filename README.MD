# 基于众包车位信息服务模式探索研究(单体)
微服务版本: [基于众包车位信息服务模式探索研究(微服务)](https://github.com/thesomeexp/ParkingCloud)

该项目是通过众包方式采集各公共停车位相关数据, 建立停车位拥挤度模型, 并向用户提供停车位的拥挤度信息. 项目后端由我独立开发. 它使用SpringBoot + Thymeleaf 混合 Vue.js 做架构, Bootstrap做页面美化, 使用GeoHash做停车位坐标存储和查询, Redis做中间缓存以降低数据库压力, Shiro框架做用户和管理员的权限控制.  对XSS, CSRF, 重放攻击并做了简单的防御. 

## 该SpringBoot项目说明
### 1.先配置安装maven：

SpringBoot默认JDK1.8

数据库我用的是mysql-5.7.28, mysql5.5-5.7应该都可以吧,如果是高版本的mysql要改一下pom.xml, 暂时不要用8.0

静态资源webapp/static/*

动态资源webapp/data/*

配置resources/application.properties

数据库表resources/parking.sql 数据库使用utf8mb4编码
### 2.目录说明
源代码路径：/src

### /src/main里
java源程序：/java
springboot全局配置：application.properties(这里配置了数据库默认用户名root密码admin)
logback 日志配置：logback-boot.xml
sql数据库：parking.sql
网页需要引入的js，css，停车场的img等：/webapp/static
我用来测试的页面：/templates

### /src/main/java源程序里
只需要运行Application就行，springboot自带tomcat
然后访问：
http://127.0.0.1:8080/入测试页面, 对应的映射关系在PageController

如果你需要修改对应url映射的html页面修改PageController就行。
然后日志输出默认路径是/tmp/parking_info/tomcat-log/DenseTubeManage
需要修改日志输出路径在logback-boot.xml文件的第15行
