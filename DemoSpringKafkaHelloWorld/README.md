### How to run the example

**Step 1** Run a kafka4dev container 

	$ docker pull howiesynguyen/kafka4dev
	$ docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_LISTENERS=localhost:9092 --name kafka4dev howiesynguyen/kafka4dev

Ref: https://hub.docker.com/r/howiesynguyen/kafka4dev

**Step 2** Run this Spring Boot app

	$ mvnw spring-boot:run
	
	...
	2021-07-13 08:41:03.243  INFO 5384 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8081 (http)
	2021-07-13 08:41:03.251  INFO 5384 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
	2021-07-13 08:41:03.251  INFO 5384 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.48]
	2021-07-13 08:41:03.318  INFO 5384 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
	2021-07-13 08:41:03.318  INFO 5384 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 781 ms
	2021-07-13 08:41:03.772  INFO 5384 --- [           main] o.a.k.clients.consumer.ConsumerConfig    : ConsumerConfig values: 
		allow.auto.create.topics = true
		auto.commit.interval.ms = 5000
		auto.offset.reset = earliest
		bootstrap.servers = [localhost:9092]
		check.crcs = true
		client.dns.lookup = use_all_dns_ips
		client.id = consumer-demogroup-1
		...
		2021-07-13 08:41:06.628  INFO 5384 --- [nio-8081-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 2.7.1
	2021-07-13 08:41:06.628  INFO 5384 --- [nio-8081-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 61dbce85d0d41457
	2021-07-13 08:41:06.628  INFO 5384 --- [nio-8081-exec-1] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1626140466628
	2021-07-13 08:41:06.637  INFO 5384 --- [ad | producer-1] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-1] Cluster ID: iIG39uCjSeezBkeS2YRJpg

**Step 3** Submit a message via URL http://localhost:8081/helloworld

	$ curl -X POST -F message="Hello" http://localhost:8081/helloworld

**Step 4** Go to the console and check the log messages

	2021-07-13 08:43:27.870  INFO 5384 --- [nio-8081-exec-2] hsnguyen.demo.springkafka.KafkaProducer  : Producing message: Hello
	2021-07-13 08:43:27.876  INFO 5384 --- [ntainer#0-0-C-1] hsnguyen.demo.springkafka.KafkaConsumer  : Consumed message: Hello