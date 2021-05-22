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

package hsnguyen.demo.spring.petcarebackend;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import hsnguyen.demo.spring.petcarebackend.entity.Owner;
import hsnguyen.demo.spring.petcarebackend.entity.Pet;
import hsnguyen.demo.spring.petcarebackend.jpa.OwnerJPARepository;
import hsnguyen.demo.spring.petcarebackend.jpa.PetJPARepository;


@SpringBootApplication
public class PetCareRestapiApplication {
	private static final Logger log = LoggerFactory.getLogger(PetCareRestapiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PetCareRestapiApplication.class, args);
	}

	@Bean
    ApplicationRunner init(OwnerJPARepository ownerRepos, PetJPARepository petRepos) {
		log.info("Loading some initial sample data for demonstration purpose");
        return args ->  { 
        	ownerRepos.save(new Owner(1, "Howie S. Nguyen", "Mars"));
           	petRepos.save(new Pet("Rio", "Bird", new Owner(1)));
        	petRepos.save(new Pet("Puss in Boots", "Cat", new Owner(1)));
        	petRepos.save(new Pet("Frankenweenie ", "Dog", new Owner(1)));
        	
        	ownerRepos.save(new Owner(2, "Iron Man", "US"));
        	petRepos.save(new Pet("Scooby-Doo", "Dog", new Owner(2)));
        	
        	ownerRepos.save(new Owner(3, "Thanos", "Titan"));      	
        	petRepos.save(new Pet("Kitty", "Cat", new Owner(3)));
        };
    } 


}
