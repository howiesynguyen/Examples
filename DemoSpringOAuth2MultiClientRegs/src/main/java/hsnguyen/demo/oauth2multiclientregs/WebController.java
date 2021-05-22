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
package hsnguyen.demo.oauth2multiclientregs;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class WebController {
	
	
	@RequestMapping("/welcome")
	public String welcomePage(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User) {
		
		/*
		 * After a successful authentication with an external OAuth 2 service, 
		 * the Authentication object kept in the security context is actually an OAuth2AuthenticationToken which, 
		 * along with help from OAuth2AuthorizedClientService can avail us with an access token for making requests against the service’s API.
		 * */	
		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		model.addAttribute("accessToken", accessToken);
		
		model.addAttribute("userName", oauth2User.getName());
		model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
		model.addAttribute("userAttributes", oauth2User.getAttributes());
		return "welcome";
	}

	@RequestMapping("/")
	public String index(Model model, Principal principal) {
		return "index";
	}
	
	
	@RequestMapping(value="/loggedout")
	public String logoutPage () {
	    return "loggedout";
	}
	
}
