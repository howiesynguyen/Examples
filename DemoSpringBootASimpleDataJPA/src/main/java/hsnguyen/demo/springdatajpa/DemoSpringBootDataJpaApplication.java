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
package hsnguyen.demo.springdatajpa;

import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import hsnguyen.demo.springdatajpa.entity.Pet;
import hsnguyen.demo.springdatajpa.jpa.PetRepository;

@SpringBootApplication
public class DemoSpringBootDataJpaApplication {
	private static final Logger log = LoggerFactory.getLogger(DemoSpringBootDataJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootDataJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner demoCommandLineRunner(PetRepository petRepository) {
		return (args) -> {
			// save a few customers
			petRepository.save(new Pet("Meow", "Cat"));
			petRepository.save(new Pet("Woof", "Dog"));
			petRepository.save(new Pet("Chirp", "Bird"));
			petRepository.save(new Pet("Kitty", "Cat"));
			petRepository.save(new Pet("Doraemon", "Cat"));

			// fetch all pets
			log.info("List of all pets:");
			StreamSupport.stream(petRepository.findAll().spliterator(), false)
					.map(pet -> pet.toString())
					.forEach(s -> log.info(s));
			// or instead of using stream api, we can write it like this:
			// for (Pet pet : repository.findAll()) {
			// log.info(pet.toString());
			// }

			// fetch all cats
			log.info("Cats:");
			petRepository.findByType("Cat").stream().forEach(cat -> log.info(cat.toString()));

		};

	}

}
