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

package hsnguyen.demo.spring.petcarebackend;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import hsnguyen.demo.spring.petcarebackend.entity.Owner;
import hsnguyen.demo.spring.petcarebackend.entity.Pet;
import hsnguyen.demo.spring.petcarebackend.hateoas.OwnerRepresentationModel;
import hsnguyen.demo.spring.petcarebackend.hateoas.PetRepresentationModel;
import hsnguyen.demo.spring.petcarebackend.jpa.OwnerJPARepository;
import hsnguyen.demo.spring.petcarebackend.jpa.PetJPARepository;

/**
 * In this example, this is the main class is to handle HTTP requests from clients based in Spring Web MVC
 * It defines mappings for HTTP requests, i.e map requests to corresponding methods that handle/process the requests
 * <br/><br/>
 * <b>Note:</b> The example code should be used as a reference for learning purposes. 
 * So I keep the design and implementation simple as much as possible, 
 * ignore some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas of how to use Spring HATEOSA
 * If you work on a project for production, you should consider to create a better design and implementation for your own, 
 * and apply best practices.
 * <br/><br/>
 * @author	Nguyen, Hung (Howie)
 * @since	2021 
*/

// To check this API, you can use RESTAPI Client such as Postman API Client or curl or HTTPie,...
// http://<servername>:<port>/<content-path>/<resource path>
// By default content-path is "/". In this example, I change it to "/api/petcare/" . 
// and it is done by declaring the property server.servlet.context-path=/api/petcare in application.properties
// So if you run this code on your local, you could submit a request like this, for example HTTP GET http://localhost:8080/api/petcare/owners

//@RestController = @Controller + @ResponseBody 
//@ResponseBody indicates that a method return value will be written into the web response body
//Also Jackson JSON library is included by default by the web starter, it help automatically marshal objects that are returned from request handler methods, into JSON. 
@RestController 
public class PetCareController {
	
	@Autowired
	private OwnerJPARepository ownerRepos;
	
	@Autowired
	private PetJPARepository petRepos;
	
	// HTTP GET 
	@GetMapping("/owners/{id}")
	public ResponseEntity<OwnerRepresentationModel> getOwnerById(@PathVariable("id") int id) {

		// fetch from database and save into an Owner entity
		Owner owner = ownerRepos.findById(id);

		//Using ResponseEntity to support HTTP status codes
		if (owner == null) {
			//HTTP status code 404 Not Found
			return ResponseEntity.notFound().build();
		} else {
			// convert Owner to RepresentationModel so that we can take advantages of the
			// Spring HATEOAS
			OwnerRepresentationModel ownerRepresentationModel = new OwnerRepresentationModel(owner.getId(),
					owner.getName(), owner.getContactInfo());

			// To a add a hypermedia link section like this: "pets":{"href":"http://localhost:8080//api/petcare/owners/1/pets"} to the response
			// We can write a statement like this:
			Link petsLink = Link.of("http://localhost:8080/api/petcare/owners/" + id + "/pets", "pets");

			//Hardcoding Link values isn't a good idea.
			// In practice, we should let the code generates the link dynamically by using the methods such as methodOn, etc... 
			//provided by WebMvcLinkBuilder, in conjunction with your controller. 
			//So instead of Link.of("http://localhost:8080/api/petcare/owners/" + id), it looks like below:
			Link selfLink = linkTo(methodOn(PetCareController.class).getOwnerById(id)).withSelfRel();


			// Now, we have 2 links petsLink and selfLink. Let add them to the RepresentationModel..
			// 1st link
			ownerRepresentationModel.add(selfLink);
			// 2nd link
			ownerRepresentationModel.add(petsLink);

			//Use ResponseEntity.ok() to include the HTTP status code 200 to the response
			return ResponseEntity.ok(ownerRepresentationModel);

			//You can also use EntityModel to wrap the object ownerRepresentationModel.
			//If so, the return statement would look like this: return ResponseEntity.ok(EntityModel.of(ownerRepresentationModel, selfLink, petsLink));
			//and if so, don't have to invoke ownerRepresentationModel.add(selfLink) and ownerRepresentationModel.add(petsLink) as above
			//Also, the method declaration look like this: public ResponseEntity<EntityModel<OwnerRepresentationModel>> getOwnerById(@PathVariable("id") int id)

		}

	}
			
	//HTTP GET
	@GetMapping("/owners")
	public ResponseEntity<CollectionModel<OwnerRepresentationModel>> getAllOwners() {
		Iterable<Owner> ownerIte = ownerRepos.findAll();
		
		List<OwnerRepresentationModel> ownerRepresentationModels = new ArrayList<OwnerRepresentationModel>();
		ownerIte.forEach(owner -> {
			OwnerRepresentationModel ownerRM= new OwnerRepresentationModel(owner.getId(), owner.getName(), owner.getContactInfo());
			ownerRepresentationModels.add(ownerRM);
	    });
		
		//add hypermedia links for each owner in the list
		ownerRepresentationModels.forEach(ownerRM -> {
			//"_self" 
			ownerRM.add(linkTo(methodOn(PetCareController.class).getOwnerById(ownerRM.getId())).withSelfRel());
			
			//"slash("pets")" is to add /pets ...
			ownerRM.add(linkTo(methodOn(PetCareController.class).getOwnerById(ownerRM.getId())).slash("pets").withRel("pets"));
			
			//Instead of using the statement above, a better way to generate the link section for /owners/{ownerId}/pets could look like this:
			//ownerDTO.add(linkTo(methodOn(PetCareController.class).getPetsForOwner(ownerDTO.getId())).withRel("pets"));
	    });
		
		//self link for the list, ie /owners
	    Link allOwnersSelfLink = linkTo(methodOn(PetCareController.class).getAllOwners()).withSelfRel();

	    //Return a CollectionModel that is used to wrap the list of OwnerRepresentationModel objects including their links, and then add a self link for the list for its own
	    return ResponseEntity.ok(CollectionModel.of(ownerRepresentationModels, allOwnersSelfLink));
	}

	
	//HTTP GET
	@GetMapping("/owners/{ownerId}/pets")
	public ResponseEntity<CollectionModel<PetRepresentationModel>> getPetsForOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = ownerRepos.findById(ownerId);
		List<Pet> pets = owner.getPets();

		List<PetRepresentationModel> petRepresentationModels = new ArrayList<PetRepresentationModel>();
	    for (final Pet pet : pets) //if needed, we can use the final keyword to make it immutable
	    {
			PetRepresentationModel petRM = new PetRepresentationModel(pet.getId(), pet.getName(), pet.getType());
	        Link selfLink = linkTo(methodOn(PetCareController.class).getPetById(pet.getId())).withSelfRel();
	        petRM.add(selfLink);
	        
	        petRepresentationModels.add(petRM);
	    }
	    
	    Link petsSelfLink = linkTo(methodOn(PetCareController.class).getPetsForOwner(ownerId)).withSelfRel();

		return ResponseEntity.ok(CollectionModel.of(petRepresentationModels, petsSelfLink));
	}

	//HTTP POST
	@PostMapping("/owners")
	public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
		//@RequestBody annotation help map the HttpRequest body to an object (DTO or domain entity or etc..)
		
		//To test this method, you can send a request HTTP POST with a body value look like this: {"id":7,"name":"Hulk","contactInfo":"US"}
		//For example, using HTTPie: $ echo {"id": 10, "name": "Hulk", "contactInfo": "US"} | http POST http://localhost:8080/owners
		//Using curl tool: $ curl -X POST "http://localhost:8080/owners" -H "accept: */*" -H "Content-Type: application/json" -d "{\"id\": 11, \"name\": \"Hulk\", \"contactInfo\": \"US\"}"
		
		ownerRepos.save(owner);
		
		//If the resource is created successfully, using the ResponseEntity will help us set HTTP response code to 201 (Created), 
		//and at the same time return content of the owner to the client
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(owner.getId()).toUri();
		return ResponseEntity.created(location).body(owner);
	}
	
	//HTTP PUT 
	@PutMapping("/owners/{id}")
	public void updateOwner(@RequestBody Owner owner, @PathVariable int id) {
		//To test this method, you can submit a request like this: 
		//HTTP PUT http://localhost:8080/owners/4 with the body content is {"name":"Bee","contactInfo":"US"} for example.

		owner.setId(id);
		ownerRepos.save(owner);
	}
	
	
	//HTTP DELETE
	@DeleteMapping("/owners/{id}")
	public void deleteOwner(@PathVariable int id) {
		//Before remove the owner, we must remove all of pet(s) that is(are) associated with this owner. 
		//If not, it may throw an Exception saying "Referential integrity constraint violation" 
		petRepos.deleteAll(petRepos.findByOwner_Id(id));

		//now we can delete the owner
		ownerRepos.deleteById(id);
		
		//By default, if no error, HTTP response code 200 will be returned to client. If needed, you can set it to other status code such as 204 (No Content) 
	}

	//HTTP GET 
	@GetMapping("/pets/{id}")
	public ResponseEntity<PetRepresentationModel> getPetById(@PathVariable("id") int id) {
	
		//Fetch from database 
		Pet pet = petRepos.findById(id);
		
		//Convert domain entity Pet to RepresentationModel 
		PetRepresentationModel petRepresentationModel = new PetRepresentationModel(pet.getId(), pet.getName(), pet.getType(), pet.getOwner().getId());
		
	    Link selfLink = linkTo(methodOn(PetCareController.class).getPetById(id)).withSelfRel();
	    petRepresentationModel.add(selfLink);

		return ResponseEntity.ok(petRepresentationModel);
		
	}
	
	//HTTP GET 
	@GetMapping("/pets")
	public ResponseEntity<CollectionModel<PetRepresentationModel>> getAllPets() {
		Iterable<Pet> petIte = petRepos.findAll();

		List<PetRepresentationModel> petRepresentationModels = new ArrayList<PetRepresentationModel>();
	    for (Pet pet : petIte) 
	    {
			PetRepresentationModel petRM = new PetRepresentationModel(pet.getId(), pet.getName(), pet.getType(), pet.getOwner().getId());
	        Link selfLink = linkTo(methodOn(PetCareController.class).getPetById(pet.getId())).withSelfRel();
	        petRM.add(selfLink);
	        
	        petRepresentationModels.add(petRM); 
	    }
	    
	    Link petsSelfLink = linkTo(methodOn(PetCareController.class).getAllPets()).withSelfRel();

		return ResponseEntity.ok(CollectionModel.of(petRepresentationModels, petsSelfLink));
	}
	
	//TODO you can continue to define additional methods for CREATE, UPDATE, DELETE for Pet...
 
}
