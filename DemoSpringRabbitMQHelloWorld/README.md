
**Run an RabbitMQ server**

docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:3.9.1-management

Note: Admin console: http://localhost:15672 
(Default username and password: “guest”)


**application.properties**

	spring.rabbitmq.host=localhost
	spring.rabbitmq.port=5672
	spring.rabbitmq.username= guest
	spring.rabbitmq.password= guest
	
**Run the example**

mvnw spring-boot:run