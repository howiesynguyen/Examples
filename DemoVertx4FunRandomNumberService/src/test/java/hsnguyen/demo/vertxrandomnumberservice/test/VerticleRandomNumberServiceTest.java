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
package hsnguyen.demo.vertxrandomnumberservice.test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hsnguyen.demo.vertxrandomnumberservice.VerticleRandomNumberService;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class VerticleRandomNumberServiceTest {
	private static Logger logger = LoggerFactory.getLogger(VerticleRandomNumberServiceTest.class);
	
	/**
	 * Deploy the verticle and execute the test methods when the verticle is
	 * successfully deployed
	 */
	@BeforeEach
	void setUp(Vertx vertx, VertxTestContext testContext) {
		vertx.deployVerticle(new VerticleRandomNumberService(), testContext.succeedingThenComplete());
	}

	
	@Test
	@Timeout(value = 10, timeUnit = TimeUnit.SECONDS) //specific a timeout rather than using a default timeout value
	@DisplayName("Test api.randomnumber")
	void random_number(Vertx vertx, VertxTestContext testContext) {
		
		//in vertx 4, eventBus().request() with a reply handler is used instead of send()
		vertx.eventBus().<JsonObject>request("api.randomnumber", null, new DeliveryOptions(), 
				asyncResultEvent -> { 
					if(asyncResultEvent.succeeded())
					{
						logger.info("Requested api.randomnumber successfully"); 
						logger.info("Response content: " + asyncResultEvent.result().body());
						
						JsonObject jsonResult = asyncResultEvent.result().body();
						
				        testContext.verify(() -> {
				        	//if not using verify() to wrap assertThat() calls and completeNow(),
				        	//then completeNow() causes a success test even there is an assertThat() says failed
				        	//if not calling completeNow(), then it it wait until timeout to finish the server and mark as failed by Timeout
				        	//therefore, verify() is recommended to be used
							assertThat(jsonResult, is(notNullValue()));
							assertThat(jsonResult.getInteger("randomnumber"), is(notNullValue()));
							testContext.completeNow();

				        });
					}
					else
						testContext.failNow("The request failed");
				});
	}

	@AfterEach
	void tearDown(Vertx vertx, VertxTestContext testContext) {
        vertx.close(testContext.succeeding(response -> {
            testContext.completeNow();
        }));
	}
}
