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
package hsnguyen.demo.springbootwebflux;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@Controller
class DemoControllerAndRouter {
	/**
	 * This demo uses RouterFunction, which is a functional alternative to the RequestMapping/GetMapping and Controller annotations used in standard Spring MVC.
	 * RequestMapping and Controller annotations are still valid in WebFlux 
	 * */
	  @Bean
	  public RouterFunction<ServerResponse> hellorouterfunctionRoute(DemoHandler demoHandler) {
	    return RouterFunctions
	        .route(RequestPredicates.GET("/hellorouterfunction")
	        		.and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), demoHandler::hellorouterfunction);
	  }
	  
	  /**Or you can simply implement a route like this*/
	  @Bean
	  public RouterFunction<ServerResponse> helloRoute() {
	    return route(RequestPredicates.GET("/hello"), 
	    		request -> ServerResponse.ok()
	    			.contentType(MediaType.TEXT_PLAIN)
	    			.body(BodyInserters.fromPublisher(Mono.just("Hello!"), String.class)));
   	  }
	  
	  /**A demo of using WebFlux with the annotations Controller and GetMapping. 
	   * Although RouterFunction is preferred in WebFlux-based apps, 
	   * but annotations in WebMVC such as Controller and GetMapping are still valid with WebFlux**/
	  @GetMapping("/")  
	  public Mono<Void> contextRootPath(ServerWebExchange exchange) {
		  String htmlBody="<html>\r\n"
		  		+ "<head>\r\n"
		  		+ "<title>DemoSpringBootWebFluxHelloWorld</title>\r\n"
		  		+ "</head>\r\n"
		  		+ "<body>\r\n"
		  		+ "<p>You've just request the context root \"/\" handled by using @GetMapping(\"/\") with WebFlux</p>\r\n"
		  		+ "<p><a href=\"hellorouterfunction\">Click here to call REST API /hellorouterfunction</a></p>\r\n"
		  		+ "</body>\r\n"
		  		+ "</html>";

		  
		  ServerHttpResponse response = exchange.getResponse();
		  response.getHeaders().setContentType(MediaType.TEXT_HTML);
		  DataBuffer buffer = response.bufferFactory()
				  .wrap(htmlBody.getBytes());
		  return response.writeWith(Mono.just(buffer));
	  } 

}
