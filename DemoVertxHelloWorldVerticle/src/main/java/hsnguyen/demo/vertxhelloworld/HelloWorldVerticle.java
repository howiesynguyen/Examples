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
package hsnguyen.demo.vertxhelloworld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class HelloWorldVerticle extends AbstractVerticle {
	private static Logger logger = LoggerFactory.getLogger(HelloWorldVerticle.class);

	@Override
	public void start(Promise<Void> promise) throws Exception {
		Router router = Router.router(vertx);

		// Use BodyHandler to retrieve the request body for all routes
		router.route().handler(BodyHandler.create());

		// REST API
		router.get("/hello").handler(this::sayHelloHandler);

		// set up an http server
		logger.info("Server is starting on port 8080...");
        HttpServer httpserver = vertx.createHttpServer();
        httpserver.requestHandler(router).listen(8080)
        	.onSuccess(server -> logger.info("Server started and listening on port {}", server.actualPort()))
            .onFailure(server -> logger.info("Failed to start the server"));
		
        // Complete the promise when the Verticle is ready. 
		//It is useful in some cases to omit a "complete" event. 
		//For examples, if not errors like this "java.util.concurrent.TimeoutException: The test execution timed out. Make sure your asynchronous code includes calls to either VertxTestContext#completeNow(), VertxTestContext#failNow() or Checkpoint#flag()"
		//could happen when running unit testing 
		promise.complete();
	}

	private void sayHelloHandler(RoutingContext ctx) {
		logger.info("Processing the request /hello ... ");
		HttpServerResponse response = ctx.response();
		response.putHeader("content-type", "text/plain");
		response.end("Hello World from HelloWorldVerticle!");
	}

	/**Convenient method for development/testing purpose. 
	 * For further testing, refer to classes in hsnguyen.demo.vertxhellowolrd.test */
	public static void main(String[] args) {
		// a very simple deployment
		logger.info("Deploying HelloWorldVerticle...");

		Vertx.vertx().deployVerticle(hsnguyen.demo.vertxhelloworld.HelloWorldVerticle.class.getName());
	}

}
