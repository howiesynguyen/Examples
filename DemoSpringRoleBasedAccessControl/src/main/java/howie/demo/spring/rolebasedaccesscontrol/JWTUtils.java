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

import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

/**
 * <b>Note:</b> This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example ignores some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 * <br/>
 * @author	Nguyen, Hung (Howie)
 * @since	2021 
*/

public class JWTUtils {
	
	public static JWT convertToJWT(String token) throws ParseException
	{

		JWT jwt = JWTParser.parse(token);
		return jwt;
	}

	
	/**For Okta provider*/
	public static List<String> extractGroupsClaim(JWT jwt) throws ParseException
	{
		JWTClaimsSet jwtClaimSet = jwt.getJWTClaimsSet();
		List<String> groups = jwtClaimSet.getStringListClaim("groups");
		
		return groups;
	}
	
	public static String decodePayload(String token) throws ParseException
	{
		String[] jwtparts = token.split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(jwtparts[1]);
		return new String(decodedBytes);
	}
	
	public static String decodeHeader(String token) throws ParseException
	{
		String[] jwtparts = token.split("\\.");
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] decodedBytes = decoder.decode(jwtparts[0]);
		return new String(decodedBytes);
	}
	
	public static List<GrantedAuthority> toAuthorities(List<String> roles) throws ParseException {
		
		List<GrantedAuthority> grantedAuthorities = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.toUpperCase()))
				.collect(Collectors.toList());
		
		return grantedAuthorities;
	}

	
	//convenient method for dev purpose
	public static void main(String[] args) {
		String exampleToken = "eyJraWQiOiJfSUJ4NUg5UHI2dVhnbUlVNF9jWVNPdmJtaXkxd0g4MTdxVEhhSk5UaGpJIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULlBWM3Nvd0FjVDRGT0FrUE40ekxuMjdhdVhGdWVTa1BpNXRvendqYnhiNW8iLCJpc3MiOiJodHRwczovL2Rldi0wMzYwNTY2OS5va3RhLmNvbS9vYXV0aDIvZGVmYXVsdCIsImF1ZCI6ImFwaTovL2RlZmF1bHQiLCJpYXQiOjE2MTk3NDUzOTAsImV4cCI6MTYxOTc0ODk5MCwiY2lkIjoiMG9hbWw3eW5xVnJ2RHdSR0w1ZDYiLCJ1aWQiOiIwMHVtbGJjanZyaUJGalFqUzVkNiIsInNjcCI6WyJlbWFpbCIsInByb2ZpbGUiLCJvcGVuaWQiXSwic3ViIjoiaG5ndXllbnR2QGdtYWlsLmNvbSIsImdyb3VwcyI6WyJFdmVyeW9uZSIsImdyb3VwYiJdfQ.hiGVwbLAb99JHdzWHua-e_mj5RZ6Ukf1txkh4_QCSGUIkB78uJ1AIdZNnmi1c7GllMvPARKmLWZ5c4e0Sy4v7ANPHMwrvWysRoaPKNPB1uUdhDePGPkqF_gOm-VsqBFA7b69NVNHFReI6CKAO9npuDo15pLBBldvZ2MBz6VqQyHV1cW4AcjtzmpA48dyFcaaSRxqYykDeR7BE1sH4ftQZT-hDqhjFjiytqYAaqTfHn5jeVbrPrGKe0aOnCNeR5MMZP1xdeEDKnvwyiQf_cxllaRkMZtolRv5btXlvdYO_HfFgW4Kp-8acR2h1Vi9Y4THBAs7Bxh7nqBf3uov-kxKzQ";
		
		try {
			System.out.println(JWTUtils.decodePayload(exampleToken));
			
			JWT jwt = JWTParser.parse(exampleToken);
			System.out.println(jwt.getParsedString());
			
			
			List<String> groups = JWTUtils.extractGroupsClaim(JWTUtils.convertToJWT(exampleToken));
			groups.stream().forEach(System.out::println);
			
			JWTUtils.toAuthorities(groups).stream().forEach(authority -> {
				System.out.println("Authority: " + authority.getAuthority());
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
