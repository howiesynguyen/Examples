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
package hsnguyen.demo.vertxsaysomethingservice;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.spi.cluster.hazelcast.ClusterHealthCheck;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;


/**This is a backend service using point-2-point (or request-response) via EventBus*/
public class VerticleSaySomethingService extends AbstractVerticle {
	private static Logger logger = LoggerFactory.getLogger(VerticleSaySomethingService.class);
	
    @Override
    public void start(Promise<Void> promise) throws Exception {
        Router router = Router.router(vertx);
        
        router.get("/health").handler(rc -> rc.response().end("Alive"));
     
        Handler<Promise<Status>> procedure = ClusterHealthCheck.createProcedure(vertx);
        HealthChecks checks = HealthChecks.create(vertx).register("cluster-health", procedure);
        router.get("/readiness").handler(HealthCheckHandler.createWithHealthChecks(checks));

		logger.info("An HTTP server is starting for HealthChecks...");
        HttpServer httpserver = vertx.createHttpServer();
        //indicate port 0 to ask the system for finding a suitable port number
        httpserver.requestHandler(router).listen(0)
        	.onSuccess(server -> logger.info("HealthChecks server started and listening on port {}", server.actualPort()))
            .onFailure(server -> logger.info("Failed to start the HealthChecks server"));
        
        //Create a consumer and register it against the event (request) api.saysomething
        vertx.eventBus().<String>consumer("api.saysomething", replymsg -> {
        	//in vertx 4, replyAndRequest() is used instead of reply()
        	
        	
        	String rndStr = RandomStringUtils.randomGraph(21);
        	String saysomethingMessage = "Hello! You're amazing! A secret string for you: " + rndStr;
        	
        	replymsg.replyAndRequest(saysomethingMessage);
        });       

        // Complete the promise when the verticle is ready. 
		//It is useful in some cases to omit a "complete" event. 
		//For examples, if not errors like this "java.util.concurrent.TimeoutException: The test execution timed out. Make sure your asynchronous code includes calls to either VertxTestContext#completeNow(), VertxTestContext#failNow() or Checkpoint#flag()"
		//could happen when running unit testing 
		promise.complete();
    }
    
    
    // Convenient method for development/testing purpose
    //For further testing, refer to hsnguyen.demo.vertxsaysomethingservice.test.VerticleSaySomethingServiceTest
    public static void main(String[] args) {
		logger.info("Deploying VerticleSaySomethingService...");
	    Vertx.clusteredVertx(new VertxOptions())
	      .compose(vertx -> vertx.deployVerticle(new VerticleSaySomethingService()))
	      .onFailure(Throwable::printStackTrace);
	  }
    
}
