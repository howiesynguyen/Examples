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
package hsnguyen.demo.spring.oauth2login;

import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@ResponseBody
public class WebController {
	
	private static String HTML_BEGIN = "<html>\n" + "<header><title>Demo-OAuth2Login</title></header>\n" 
    		+ "<body style=\"margin: 20px\">\n"
    		+ "<p><b>OAuth2Login Demonstration</b></p>\n";
	private static String HTML_END = "</body>\n" + "</html>";
	
	@RequestMapping(value = "/", produces = "text/html")
	public String rootContext() {
		String logoutHTML =	"<a href=\"/logout\">Logout</a>\n";
		
		String loginHTML;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken || auth.getName().equals("anonymousUser"))
			loginHTML = "<p>You're not logged in yet. <a href=\"login\">Login</a></p>\n" ;
		else 
			loginHTML = "<p>You're logged in. Username: " + auth.getName() + " (given by the OpenID provider)</p>\n"
						+ logoutHTML;
		
        return  HTML_BEGIN
        		+ loginHTML
        		+ "<p><a href=\"accesstoken\">View your access token</a> (Login required)</p>\n" 
        		+ HTML_END;
	}
	
	@RequestMapping(value = "/login", produces = "text/html")
	public String login() {
		//From here, to sign a user in by Okta for example, we can redirect the browser to the Okta-hosted sign-in page by the base path 
		final String oktaSignInRedirectLink = "/oauth2/authorization/okta";
		
        return  HTML_BEGIN
        		+ " <p><a href =\""+ oktaSignInRedirectLink + "\">Continue to sign in with the OpenID Connect Identity Provider</a>\n"
         		+ " <p><i>(Note: if you've already signed in via the Provider and not signed out from the Provider yet, then you won't have to sign in - ie, provide username/passworld again)</i>\n"
        		+ HTML_END;
	}

	@RequestMapping(value = "/accesstoken", produces = "text/html")
	public String accesstoken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OAuth2User oauth2User) {

		/*
		 * After a successful authentication with an external OAuth 2 service, 
		 * the Authentication object kept in the security context is actually an OAuth2AuthenticationToken which, 
		 * along with help from OAuth2AuthorizedClientService can avail us with an access token for making requests against the service’s API.
		 * */	
		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		
		String strOauth2User = oauth2User.getName();
		
		Map<String, Object> strOauth2UserAttrs = oauth2User.getAttributes();
		
		//the value will be obtained via the application file (application.properties or application.yaml), 
		//the property spring.oauth2.client.registration.{the auth server name, ex okta or github or google or facebook}.client-name
		String clientName = authorizedClient.getClientRegistration().getClientName();
		
        return  HTML_BEGIN
        		+ "<p><b>Your username:</b> " + strOauth2User + " <i>(provided by the provider " + clientName +")</i></p>\n" 
        		+ "<p><b>User attributes obtained from the authentication server (ie the provider):</b> " + strOauth2UserAttrs + "</p>\n" 
        		+ "<p><b>Your Access Token:</b> " + accessToken + "</p>\n" 
        		+ "<p><a href=\"/\">Home page</a></p>\n" 
        		+ HTML_END;
	}
	
}
