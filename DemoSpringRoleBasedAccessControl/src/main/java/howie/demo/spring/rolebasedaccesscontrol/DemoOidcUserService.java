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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * <b>Note:</b> This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example ignores some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 * <br/>
 * @author	Nguyen, Hung (Howie)
 * @since	2021 
*/
@Component
public class DemoOidcUserService extends OidcUserService {
	private static final Logger log = LoggerFactory.getLogger(DemoOidcUserService.class);

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("Executing OidcUser::loadUser ...");
		
		OidcUser user = super.loadUser(userRequest);
		String tokenValue = userRequest.getAccessToken().getTokenValue();
		log.info("Token: " + tokenValue);
		
		OidcUser newUser = user;
		
		try {
			List<String> groups = JWTUtils.extractGroupsClaim(JWTUtils.convertToJWT(tokenValue));
			groups.stream().forEach(group -> {
				log.info("Group: " + group);
			});
			List<GrantedAuthority> groupAuthorities = JWTUtils.toAuthorities(groups);
			groupAuthorities.stream().forEach(authority -> {
				log.info("Group Authority: " + authority.getAuthority());
			});
			
			Collection<? extends GrantedAuthority> userAuthorities = user.getAuthorities();
			if(userAuthorities == null)
			{
				userAuthorities= new ArrayList<GrantedAuthority>();
			}
			//because getAuthorities() returns an immutable Collection, so we can't add groupAuthorities to userAuthorities
			//therefore we add userAuthorities to groupAuthorities. And then create a new OidcUser object based on the current OidcUser and groupAuthorities 
			groupAuthorities.addAll(userAuthorities);

			
			ProviderDetails providerDetails = userRequest.getClientRegistration().getProviderDetails();
			String userNameAttributeName = providerDetails.getUserInfoEndpoint().getUserNameAttributeName();
			if (StringUtils.hasText(userNameAttributeName)) {
				newUser = new DefaultOidcUser(groupAuthorities, userRequest.getIdToken(), user.getUserInfo(), userNameAttributeName);
			}
			else
				newUser = new DefaultOidcUser(groupAuthorities, userRequest.getIdToken(), user.getUserInfo());

		} catch (ParseException e) {
			log.error("Error", e);
		}
		
		newUser.getAuthorities().stream().forEach(authority -> {
			log.info("User authority: " + authority.toString());
		});

		return newUser;
	}

}
