/*******************************************************************************
 * Copyright (c) 2021 Nguyen, Howie S. (howiesynguyen@gmail.com)
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ===========================================================
 * Note: This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example may ignore some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 *******************************************************************************/
package hsnguyen.demo.springrabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoSpringAmqpRabbitMqApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringAmqpRabbitMqApplication.class, args);
	}
	
	
	/**@param rabbitTemplate : An object of RabbitTemplate is created by the Auto-configuration of Spring Boot, and then it is injected into this param **/
	@Bean
	public ApplicationRunner appRunner(RabbitTemplate rabbitTemplate)
	{
        return args -> {
            for (int i = 0; i < 10; i++) {
        		//send msgs to the queue using the routing key helloQueue
                rabbitTemplate.convertAndSend("helloQueue", "Hello! This is message #" + i);
            }
        };
	}
	
	/**	This bean will be found by the RabbitAdmin object (created by Spring Boot auto-config).
	 * 	The RabbitAdmin object will bind the queue to the default exchange by the queue name so the queue name can be used as a routing key in the send*/
	@Bean
	public Queue helloQueue() {
		//Create a queue named helloQueue
	    return new Queue("helloQueue", false);
	}
	
	@RabbitListener(queues = "helloQueue")
	public void helloListener(String msg) {
	    System.out.println("Message from the queue helloQueue: " + msg);
	}
}
