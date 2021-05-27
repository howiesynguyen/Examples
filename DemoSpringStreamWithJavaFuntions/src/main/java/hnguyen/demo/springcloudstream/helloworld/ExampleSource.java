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

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**Source of events, i.e it's a producer*/
public class ExampleSource {
	private static final Logger log = LoggerFactory.getLogger(ExampleSource.class);
	
	@Bean
	public Supplier<Long> currentTime()
	{
		
		return () -> {
			log.info(">>> Supplier currentTime is being called...");

			return System.currentTimeMillis();
		};
	}
}
