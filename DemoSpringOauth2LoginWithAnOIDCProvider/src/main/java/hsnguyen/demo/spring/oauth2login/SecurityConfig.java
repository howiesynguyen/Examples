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
package hsnguyen.demo.spring.oauth2login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
/*The @EnableWebSecurity is to disable the default security configuration. 
The annotation is only optional if overriding the default behavior using a WebSecurityConfigurerAdapter.*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientLogoutHandler logoutHandler;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/login**", "/logout").permitAll()
				.anyRequest().authenticated()
				.and()
					.oauth2Login() //enable authentication via the OAuth2/OpenID Connect provider
					.loginPage("/login") //show our "login" page if signing in is required, don't let automatically redirect to the OAuth2/OpenID Connect provider's sign-in page
				.and()
				.logout() //Provides logout support
				.addLogoutHandler(logoutHandler)
				.logoutSuccessUrl("/") //URL to redirect to after logout has occurred, by default it is /login?logout
				.and().csrf().disable(); //in this example, we have to disable it so that the error " Forbidden 403" doesn't occur after logging out
	}

}
