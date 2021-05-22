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
package hsnguyen.demo.springreactivemongo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import reactor.core.publisher.Mono;


@SpringBootApplication
public class DemoSpringReactiveMongoApplication {
	private static final Logger log = LoggerFactory.getLogger(DemoSpringReactiveMongoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringReactiveMongoApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner demoCommandLineRunner(DocumentDAO docDAO) { 
		return (args) -> {
			log.info("===========Run demo...=============");

			docDAO.save(new MyDocument("1", "String In Action", "Content", "Howie S. Nguyen")).block();
			docDAO.save(new MyDocument("2", "Reactive Programming", "Content", "Howie S. Nguyen")).block();
			docDAO.save(new MyDocument("3", "Node.JS", "Content", "Ed")).block();
			
			log.info("Find documents that owned by Howie S. Nguyen:");
			docDAO.findAllByOwner("Howie S. Nguyen")
				.doOnNext(doc -> log.info(doc.toString()))
				.doOnError(Throwable::printStackTrace)
				.blockLast(); //use blockLast() to make sure log messages printed out in order

			log.info("Find Document that has id is 3:");
			docDAO.findById(Mono.just("3"))
					.doOnNext(doc -> log.info(doc.toString()))
					.doOnError(Throwable::printStackTrace)
					.block();
		};

	}

}
