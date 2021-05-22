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

package hsnguyen.demo.spring.petcarebackend.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Owner {
	
	@Id
	private Integer id;

	private String name;
	private String contactInfo;
	
	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
	private List<Pet> pets;
	
	//In order to use OwnerJPARepository for Owner entity, the default constructor (no arg) is required. 
	//Otherwise when invoking OwnerJPARepository, it may cause an error like this "org.hibernate.InstantiationException: No default constructor for entity"
	//Spring Data JPA requires a default constructor for each Entity
	public Owner() {
	}

	public Owner(int id) {
		this.id = id;
	}
	
	public Owner(int id, String name, String contactInfo) {
		this.id = id;
		this.name = name;
		this.contactInfo = contactInfo;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
    
	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	
}
