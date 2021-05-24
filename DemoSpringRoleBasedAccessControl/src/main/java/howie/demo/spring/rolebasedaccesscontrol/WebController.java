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
package howie.demo.spring.rolebasedaccesscontrol;

import java.security.Principal;
import java.text.ParseException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <b>Note:</b> This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example ignores some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 * <br/>
 * @author	Nguyen, Hung (Howie)
 * @since	2021 
*/
@Controller
public class WebController {

	@RequestMapping("/login")
	public String customLogin(Model model) {
			return "customlogin";
	}
	
	//Note: in order to let @PreAuthorize takes effect, @EnableGlobalMethodSecurity(prePostEnabled = true) must be included into SecurityConfig
	//otherwise, it will be ignored
	//by default, JWT access token is stored in a In-Memory service provided by Spring, we can obtain the token via by Authorized Client using @RegisteredOAuth2AuthorizedClient
	//https://stackoverflow.com/questions/65897449/how-to-get-jwt-token-in-securitycontextholder-in-spring-boot-oauth2
	@PreAuthorize("hasAuthority('GROUPB')")
	@RequestMapping("/poweruser")
	public String authenticatedWelcomePage(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		return "poweruser";
	}
	
	@RequestMapping("/accesstoken")
	public String authenticatedWelcomePage(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User) {

		/*
		 * After a successful authentication with an external OAuth 2 service, 
		 * the Authentication object kept in the security context is actually an OAuth2AuthenticationToken which, 
		 * along with help from OAuth2AuthorizedClientService can avail us with an access token for making requests against the service’s API.
		 * */	
		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		model.addAttribute("accessToken", accessToken);
		try {
			model.addAttribute("accessDecodedToken", JWTUtils.decodePayload(accessToken));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("userName", oauth2User.getName());
		model.addAttribute("userAttributes", oauth2User.getAttributes());
		
		//the value will be obtained via the application file (.properties or yaml), 
		//the property spring.oauth2.client.registration.{the auth server name, ex okta or github or google or facebook}.client-name
		model.addAttribute("clientRegistrationName", authorizedClient.getClientRegistration().getClientName());
		
		//use the  thymeleaf template "loggedin" to render the view returned to the user's web browser
		return "accesstoken";
	}
	
	

	@RequestMapping("/")
	public String homepage(Model model, Principal principal) {
		return "homepage";
	}
	
	
	@RequestMapping("/loggedout")
	public String loggedoutPage () {
	    return "loggedout";
	}
	
	@RequestMapping("/accessdenied")
	public String accessdeinied(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {

		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		model.addAttribute("accessToken", accessToken);
		try {
			model.addAttribute("accessDecodedToken", JWTUtils.decodePayload(accessToken));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "accessdenied";
	}
}
