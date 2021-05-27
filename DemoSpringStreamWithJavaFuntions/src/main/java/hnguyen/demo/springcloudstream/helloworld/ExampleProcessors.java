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

package hnguyen.demo.springcloudstream.helloworld;

import java.text.SimpleDateFormat;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

public class ExampleProcessors {
	private static final Logger log = LoggerFactory.getLogger(ExampleProcessors.class);

	@Bean
	public Function<Long, String> formatDateTime()
	{
		Function<Long, String> strTime = timeMillis -> {
			log.info(">>> Processor formatDateTime is being called...");

		    SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy z");  
		    String sTime = sfd.format(Long.valueOf(timeMillis));
			return sTime;
		};
		return strTime;
	}
	
	@Bean
	public Function<String, String> sayHelloAndTime()
	{
		return time -> {
			log.info(">>> Processor sayHelloAndTime is being called...");
			
			return "Hello! The time is now " + time;
		};
	}
}
