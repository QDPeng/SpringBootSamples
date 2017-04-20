#SpringBoot1.5.1版本集成spring-kafka自动配置

## 1、在官网下载安装 kafka_2.11-0.10.2.0

## 2、进入conf/server.properties 开启监听：listeners=PLAINTEXT://localhost:9092

## 3、通过cmd进入bin/windows目录，开启zookeeper server：

### zookeeper-server-start.bat D:\kafka_2.11-0.10.2.0\config\zookeeper.properties

## 3、通过cmd进入bin/windows目录，开启kafka server：

### kafka-server-start.bat D:\kafka_2.11-0.10.2.0\config\server.properties
