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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**Logging out of this client app. It is not a SSO Logout. 
 * SSO Logout needs to be performed with help from the OpenID Connect provider. 
 * It is out of scope in this example, so I don't do it here*/
@Service
public class ClientLogoutHandler extends SecurityContextLogoutHandler {
	private static final Logger logger = LoggerFactory.getLogger(ClientLogoutHandler.class);
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		logger.info("Executing ClientLogoutHandler::logout ...");
		
		//this super.logout() from SecurityContextLogoutHandler will invalidate session and clear Security Context
		super.logout(request, response, authentication);
		
		//remove cookie
		CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler("JSESSIONID");
		cookieClearingLogoutHandler.logout(request, response, authentication);
	}	
}
