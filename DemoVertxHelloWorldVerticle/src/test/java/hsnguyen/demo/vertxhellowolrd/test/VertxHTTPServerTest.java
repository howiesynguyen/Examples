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
package hsnguyen.demo.vertxhellowolrd.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;

public class VertxHTTPServerTest {
		@Test
		void vertx_http_server() throws Throwable {
			Vertx vertx = Vertx.vertx();

			//A test context for asynchronous executions
			VertxTestContext testContext = new VertxTestContext();

			vertx.createHttpServer().requestHandler(req -> req.response().end())
					.listen(9090)
					//onComplete() allows waiting for operations in other threads to notify of completion
					//testContext.succeedingThenComplete() creates an asynchronous result handler that expects a success to 
					//then complete the test context, ie make the test context pass.
					.onComplete(testContext.succeedingThenComplete());

			//false if the waiting delay expired before the test passed.
			assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS), is(true));

			if (testContext.failed()) {
				throw testContext.causeOfFailure();
			}
		}
}
