 **安装 MKSAPI** 
安装 mksapi 到 maven 本地仓库 mvn install:install-file -DgroupId=com.ptc.ilm -DartifactId=mksapi -Dversion=12.3.1.0 -Dpackaging=jar -Dfile=D:/mksapi.jar

 **Eclipse 启动配置** 
![输入图片说明](https://images.gitee.com/uploads/images/2021/1021/102801_209d3b5d_9896725.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/1021/102830_b1d8f4f6_9896725.png "屏幕截图.png")
参数示例：--wrvs.client.hostname=alm.localhost.com.cn --wrvs.client.port=7001 --wrvs.client.connectUser=pjia --wrvs.client.password=pjia --server.port=9100 --wrvs.client.secretKey=!QAZ2wsx

参数说明
wrvs.client.hostname：WRVS主服务地址
wrvs.client.port：WRVS主服务端口
wrvs.client.connectUser：WRVS主服务连接用户
wrvs.client.password：WRVS主服务的连接密码
server.port：web后端暴露的接口
wrvs.client.secretKey：用于生成Token的口令