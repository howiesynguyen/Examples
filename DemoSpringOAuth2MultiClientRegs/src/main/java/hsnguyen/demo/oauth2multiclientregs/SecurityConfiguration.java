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

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    ClientRegistrationRepository clientRegistrationRepository;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// note: requests to "/" and "login" are public
		http.antMatcher("/**").authorizeRequests().antMatchers("/", "/login**","/loggedout","/logout", "/webjars/**").permitAll().anyRequest().authenticated()
				.and().oauth2Login();
		
		//logout() is to enable Logout functionality - the default logout mechanism which is configured to use the following logout url /logout
		//logoutUrl("...") is to add customizations to logout
		//csrfToken is to protect the user from Cross Site Request Forgery (CSRF)
		//Also the logout stuff is applied to this client app only - not on the authentication server side
		http.logout()
		.logoutSuccessUrl("/loggedout")
		.invalidateHttpSession(true)
        .clearAuthentication(true)
		.deleteCookies("JSESSIONID")
		.and()
        .csrf()
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}


}
