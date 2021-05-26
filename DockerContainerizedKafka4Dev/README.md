**An Apache Kafka setup using Docker for development purposes**

At this time Kafka (version 2.8.0 and earlier) still requires ZooKeeper. To keep it simple, a single image is used for both ZooKeeper and Kafka

To build an image and run a container, execute the following commands:

$ docker build -t kafka4dev .

docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_LISTENERS=<hostname>:9092 --name kafka4dev kafka4dev

If it is started successfully, you may see something like this:

```
$docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_LISTENERS=localhost:9092 --name kafka4dev howiesynguyen/kafka4dev
Executing kafka-start.sh...
Set advertised.listeners: 192.168.1.2:9092 in /opt/kafka/config/server.properties
/usr/lib/python2.7/dist-packages/supervisor/options.py:461: UserWarning: Supervisord is running as root and it is searching for its configuration file in default locations (including its current working directory); you probably want to specify a "-c" argument specifying an absolute path to a configuration file for improved security.
  'Supervisord is running as root and it is searching '
2021-05-26 02:52:10,659 CRIT Supervisor is running as root.  Privileges were not dropped because no user is specified in the config file.  If you intend to run as root, you can set user=root in the config file to avoid this message.
2021-05-26 02:52:10,659 INFO Included extra file "/etc/supervisor/conf.d/supervisord.conf" during parsing
2021-05-26 02:52:10,668 INFO RPC interface 'supervisor' initialized
2021-05-26 02:52:10,668 CRIT Server 'unix_http_server' running without any HTTP authentication checking
2021-05-26 02:52:10,668 INFO supervisord started with pid 9
2021-05-26 02:52:11,673 INFO spawned: 'zookeeper' with pid 12
2021-05-26 02:52:11,682 INFO spawned: 'kafka' with pid 13
2021-05-26 02:52:12,808 INFO success: zookeeper entered RUNNING state, process has stayed up for > than 1 seconds (startsecs)
2021-05-26 02:52:12,808 INFO success: kafka entered RUNNING state, process has stayed up for > than 1 seconds (startsecs)
```

The following is an example of a configuration from a Spring Boot with Spring Cloud Stream app that uses this containerized kafka

```
application.yml
spring:
  cloud:
    stream:
      kafka:
        binder:
          brokers: localhost:9092
          replicationFactor: 1
```
