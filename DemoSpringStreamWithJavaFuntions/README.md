
### About this example

This example shows you how to use Spring Cloud Stream with Java Functions in favor of functional programming

"Spring Cloud Stream is a framework for building highly scalable event-driven microservices connected with shared messaging systems" - spring.io

References: 

https://spring.io/blog/2020/07/13/introducing-java-functions-for-spring-cloud-stream-applications-part-0

https://spring.io/blog/2020/07/20/introducing-java-functions-for-spring-cloud-stream-applications-part-1


Since Spring Cloud version 2020.0.2 (Spring Cloud Stream v3.x), the following @EnableBinding, @Input, @Output, @StreamListener... annotations have been deprecated

Instead, Java Functions feature has been introduced to take advantage of functional programming model in Spring Cloud Stream v3.x. Spring Cloud Stream can bind directly to the input(s) and output(s) of a Function @Bean. The logical equivalence of the java.util.function types Supplier, Function, Consumer are mapped to the Spring Cloud Stream concepts Source, Processor, and Sink respectively

The Spring Cloud Stream pipeline in this example looks like this:

source | processor1 |  processor2 | sink

is logically equivalent to 

supplier | function 1 | function 2 | sink 

in functional programming


If you succesfully run the example, you may see something like below printed out:
	
	...
	2021-05-27 09:59:40.268  INFO 16780 --- [container-0-C-1] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 2.7.1
	2021-05-27 09:59:40.268  INFO 16780 --- [container-0-C-1] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 61dbce85d0d41457
	2021-05-27 09:59:40.268  INFO 16780 --- [container-0-C-1] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1622084380268
	2021-05-27 09:59:40.277  INFO 16780 --- [ad | producer-4] org.apache.kafka.clients.Metadata        : [Producer clientId=producer-4] Cluster ID: iIG39uCjSeezBkeS2YRJpg
	2021-05-27 09:59:40.291  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleSink             : >>> Consumer receiveMessage is being called for the message: 09:59:40 Thu, 27 May 2021 ICT
	2021-05-27 09:59:41.235  INFO 16780 --- [   scheduling-1] h.d.s.helloworld.ExampleSource           : >>> Supplier currentTime is being called...
	2021-05-27 09:59:41.246  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleProcessors       : >>> Processor formatDateTime is being called...
	2021-05-27 09:59:41.253  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleSink             : >>> Consumer receiveMessage is being called for the message: 09:59:41 Thu, 27 May 2021 ICT
	2021-05-27 09:59:42.245  INFO 16780 --- [   scheduling-1] h.d.s.helloworld.ExampleSource           : >>> Supplier currentTime is being called...
	2021-05-27 09:59:42.256  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleProcessors       : >>> Processor formatDateTime is being called...
	2021-05-27 09:59:42.265  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleSink             : >>> Consumer receiveMessage is being called for the message: 09:59:42 Thu, 27 May 2021 ICT
	2021-05-27 09:59:43.252  INFO 16780 --- [   scheduling-1] h.d.s.helloworld.ExampleSource           : >>> Supplier currentTime is being called...
	2021-05-27 09:59:43.266  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleProcessors       : >>> Processor formatDateTime is being called...
	2021-05-27 09:59:43.273  INFO 16780 --- [container-0-C-1] h.d.s.helloworld.ExampleSink             : >>> Consumer receiveMessage is being called for the message: 09:59:43 Thu, 27 May 2021 ICT
	...