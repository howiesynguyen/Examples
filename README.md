# Demonstration code/Examples

This repository (https://github.com/howiesynguyen/Java-basedExamples) contains mostly Java-based demonstration code/examples. It should be used as a reference for learning purposes. The design and implementation are kept simple as much as possible. The examples may ignore some concerns such as unit testing, error handling, logging, and etc… It may not be a good practice, but hopefully it could give you some ideas

**DemoSpringHypermediaDrivenRESTAPI**:
An example of how to use Spring HATEOAS to create a hypermedia-driven RESTAPI - a simple backend service that can be used to manage a list of owners and their pets. 

**DemoSpringOAuth2MultiClientRegs**

**DemoSpringOauth2LoginWithAnOIDCProvider**

**DemoSpringRoleBasedAccessControl**

**DemoSpringBootWebFluxHelloWorld**

**DemoSpringBootWebFluxOAuth2Login**

**DemoSpringReactiveMongo**

**DemoVertxHelloWorldVerticle**

**DemoVertx4FunFrontendService**,
**DemoVertx4FunRandomNumberService**,
**DemoVertx4FunSaySomethingService**

**DemoSpringStreamWithJavaFuntions** This example shows you how to use Spring Cloud Stream with Java Functions in favor of functional programming. "Spring Cloud Stream is a framework for building highly scalable event-driven microservices connected with shared messaging systems" (spring.io).

**DockerContainerizedKafka4Dev** An Apache Kafka setup using Docker for development purposes

**DemoJsonHttpServer** An example of using the following

- HttpServer (JDK)
- JSON In Java (JSON.org)
- Apache HttpClient
 
The example provides a simple calculator via an HTTP server that accepts JSON requests via the URL http://localhost:8081/calculator </br>
for example, HTTP POST: {"operator":"SUBTRACTION", "a":1, "b":2} </br>
and then return a JSON response, for example {"result":3.0, "operator":"SUBTRACTION", "a":1.0, "b":2.0}

Try executing JUnit Test class CalculatorTest to see how to run it

*(this document is still being updated...)*
