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
 *******************************************************************************/
package hsnguyen.demo.springbootwebflux.oauth2login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * This demo uses RouterFunction, which is a functional alternative to the RequestMapping and Controller annotations used in standard Spring MVC.
 * RequestMapping and Controller annotations are still valid in WebFlux  <br/> <br/>
 * <b>Note:</b> This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example ignores some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 * <br/>
 * @author	Nguyen, Hung (Howie)
 * @since	2021 
 **/
@Configuration
@Controller
class Router {
	
    private static Logger log = LoggerFactory.getLogger(Router.class);
	
	/**After a successful authentication with an external OAuth 2 service, 
	 * the Authentication object kept in the security context is actually an OAuth2AuthenticationToken which, 
	 * along with help from OAuth2AuthorizedClientService can avail us with an access token for making requests against the service’s API.
	 * There will be an OAuth2AuthorizedClientService automatically configured as a bean in the Spring application context, 
	 * so you’ll only need to inject it into wherever you’ll use it.
	 * */
	
	  @GetMapping("/")  
	  public Mono<Void> contextRootPath(ServerWebExchange exchange) {
		  String htmlBody="<html>\r\n"
		  		+ "<head>\r\n"
		  		+ "<title>DemoSpringBootWebFluxHelloWorld</title>\r\n"
		  		+ "</head>\r\n"
		  		+ "<body>\r\n"
		  		+ "<p>You've just request the context root \"/\" handled by using @GetMapping(\"/\") with WebFlux</p>\r\n"
		  		+ "<p><a href=\"hellooauth2\">Request the page /hellooauth2</a></p>\r\n"
		  		+ "<p><a href=\"accesstoken\">Request the page /accesstoken</a></p>\r\n"
		  		+ "</body>\r\n"
		  		+ "</html>";

		  ServerHttpResponse response = exchange.getResponse();
		  response.getHeaders().setContentType(MediaType.TEXT_HTML);
		  DataBuffer buffer = response.bufferFactory()
				  .wrap(htmlBody.getBytes());
		  return response.writeWith(Mono.just(buffer));
	  } 
	
	
	  @Bean
	  public RouterFunction<ServerResponse> helloRoute() {
		  //instead of using  @GetMapping, @RequestMapping, or etc... of @Controller, we can use RouterFunction
		  return route(RequestPredicates.GET("/hellooauth2"), this::hellooauth2Handler);
   	  }

	  @Autowired 
	  ReactiveOAuth2AuthorizedClientService clientService;
	  
	  Mono<String> findMessageByUsername(String username) {
		    return Mono.just("Hi " + username);
	  }

	    
	  private Mono<ServerResponse> hellooauth2Handler(ServerRequest request)
	  {
		  	//Use ReactiveSecurityContextHolder instead since it's the truth source to get Security Content in WebFlux-based apps
			return ReactiveSecurityContextHolder.getContext()
					.log()
					.switchIfEmpty(Mono.error(new IllegalStateException("No SecurityContext returned from ReactiveSecurityContextHolder.getContext()")))
					.map(SecurityContext::getAuthentication).filter(auth -> {
						log.info("Check if OAuth2AuthenticationToken presents...");
						if (auth instanceof OAuth2AuthenticationToken) {
							log.info("Found OAuth2AuthenticationToken");
							return true;
						}
						log.info("Not found ");
						return false;
					})
					.cast(OAuth2AuthenticationToken.class)
					.doOnNext(authToken -> {
							//doOnNext will be triggered when the data is emitted successfully and cause no effect to the flow 
							//and returns the original Publisher(ie Mono<OAuth2AuthenticationToken> in this case) immediately
							//Use it here to just print out some a log messages for debug purpose and cause no any effect
							log.info("Client Reg. Id: " + authToken.getAuthorizedClientRegistrationId() + "; Username: " + authToken.getName());
						})
					.doOnError(Throwable::printStackTrace)
					.flatMap(/*will return a Mono<OAuth2AuthorizedClient>*/ authToken -> (Mono<OAuth2AuthorizedClient>) clientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName()))
					.doOnNext(authorizedClient -> {
						log.info("Token: " + authorizedClient.getAccessToken().getTokenValue());
					}).doOnError(Throwable::printStackTrace)
					.flatMap(a -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters
							.fromPublisher(Mono.just("You've been authenticated as user " + a.getPrincipalName() + "; Your access token: " + a.getAccessToken().getTokenValue()), String.class)));
	  }
	  
	  @GetMapping("/accesstoken")
	  Mono<Void> hello(ServerWebExchange exchange, 
			  @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			  @AuthenticationPrincipal OAuth2User oauth2User) {
		  
		  String accessToken = authorizedClient.getAccessToken().getTokenValue();
		  String oauth2UserName = oauth2User.getName();
		  
		  Map<String, Object> mapUserAttrs = oauth2User.getAttributes();
		  
		  //user Stream API to convert Map to String
		  String strUserAttrs = mapUserAttrs.keySet().stream()
			      .map(key -> key + ": " + mapUserAttrs.get(key))
			      .collect(Collectors.joining("; ", "[", "]"));
		  
		  String htmlBody="<html>\r\n"
			  		+ "<head>\r\n"
			  		+ "<title>DemoSpringBootWebFluxHelloWorld</title>\r\n"
			  		+ "</head>\r\n"
			  		+ "<body>\r\n"
			  		+ "<p>You've been authenticated by Oauth2 Login</p>\r\n"
			  		+ "<p>UserId: " + oauth2UserName + "</p>\r\n"
			  		+ "<p>User attributes: " + strUserAttrs + "</p>\r\n"
			  		+ "<p>Your access token is: <b>" + accessToken + "</b></p>\r\n"
			  		+ "</body>\r\n"
			  		+ "</html>";
		  
		  ServerHttpResponse response = exchange.getResponse();
		  response.getHeaders().setContentType(MediaType.TEXT_HTML);
		  DataBuffer buffer = response.bufferFactory()
				  .wrap(htmlBody.getBytes());
		  return response.writeWith(Mono.just(buffer));
	  }
	  
}
