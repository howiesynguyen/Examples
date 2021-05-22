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
package howie.demo.spring.rolebasedaccesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * <b>Note:</b> This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example ignores some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 * <br/>
 * @author	Nguyen, Hung (Howie)
 * @since	2021 
*/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
/*The @EnableWebSecurity is to disable the default security configuration. 
* The annotation is only optional if overriding the default behavior using a WebSecurityConfigurerAdapter.
* 
* @EnableGlobalMethodSecurity provides AOP security on methods.
* prePostEnabled = true is to make use of the annotations PreAuthorize and PostAuthorize
* If you want to use @Secured in addition, you can add securedEnabled = true, and it would look like this: EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
* I prefer to use @PreAuthorize because it's more powerful than @Secured
*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    ClientRegistrationRepository clientRegistrationRepository;
	
    @Autowired
    private CustomLogoutHandler logoutHandler;
    
    @Autowired
    OidcUserService oidcUserService;


	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.exceptionHandling()
				.accessDeniedHandler( (request,  response, accessDeniedException) -> {
					response.sendRedirect("/accessdenied");
				})
			.and()
				.authorizeRequests().antMatchers("/", "/login**", "/loggedout","/logout", "/webjars/**", "/accessdenied").permitAll()
				.anyRequest().authenticated()
			.and()
				.oauth2Login()
				.loginPage("/login")
				//https://www.devglan.com/spring-security/spring-boot-security-google-oauth
				.userInfoEndpoint()
				.oidcUserService(oidcUserService)
			.and().and()
				.logout()
				.addLogoutHandler(logoutHandler)
				.logoutSuccessUrl("/loggedout")
			//.invalidateHttpSession(true)  //logoutHandler will take care this job, so it is no needed
	        //.clearAuthentication(true) //logoutHandler will take care this job, so it is no needed
			//.deleteCookies("JSESSIONID") //logoutHandler will take care this job, so it is no needed
			.and()
		        .csrf()
		        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		
	}
}
