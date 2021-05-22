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
package hsnguyen.demo.springdatajpa.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import hsnguyen.demo.springdatajpa.entity.Pet;

/**Spring Data JPA focuses on using JPA to store data in a relational database. 
 * It has the ability to create repository implementations automatically, at runtime, from a repository interface.
 * repository interfaces here are based on the Repository pattern
 * Spring Data JPA provides some pre-defined repository interfaces that we can extend. 
 * CrudRepository is one of them. We use it in this demo because we want to do CRUD operations
 * */
public interface PetRepository extends CrudRepository<Pet, Long> {
	 /* By utilizing Spring Data JPA, you see we don't even have to provide any implementation for the methods below.
	  * Spring Data JPA will automatically creates a Repository implementation and generates queries based on method names.
	  * Of course, if needed we can customize these stuff. But in this demo, keep it simple.
	  * So with very little effort, we can make it happen :-)*/
	  List<Pet> findByName(String name);
	  List<Pet> findByType(String type);
	  Pet findById(long id);
}
