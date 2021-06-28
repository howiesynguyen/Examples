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
package hsnguyen.demo.springaop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**An example Aspect class that defines the joinpoints and the pointcuts for logging**/
@Component
@Aspect
public class MyLoggingAspect {
	private static final Logger log = LoggerFactory.getLogger(MyLoggingAspect.class);
	
	@Before("execution(* hsnguyen.demo.springaop.HelloWorld.*(..))")
	public void beforeAdvice(JoinPoint joinPoint) {
		log.info("@Before: " + joinPoint.getSignature());
		System.out.println("Before");
	}

	@After(value = "execution(* hsnguyen.demo.springaop.HelloWorld.*(..))")
	public void afterAdvice(JoinPoint joinPoint) {
		log.info("@After: " + joinPoint.getSignature());
	}
	
	@AfterReturning(value = "execution(* hsnguyen.demo.springaop.HelloWorld.*(..))", returning = "result")
    public void afterReturn(JoinPoint joinPoint, Object result){
		log.info("@AfterReturning {} returned: {}", joinPoint.getSignature(), result);
    }
	
}
