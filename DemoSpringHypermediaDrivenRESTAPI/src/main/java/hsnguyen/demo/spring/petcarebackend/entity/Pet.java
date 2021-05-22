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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * JPA entity. This entity is mapped to a table named Pet as the same class name
 */
@Entity
public class Pet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String name;
	private String type;
	
	@ManyToOne(fetch = FetchType.LAZY)
	//Give the foreign key column a name called owner_id, 
	//and it will be created for the table Pet and by default it refers to the id column id in Owner
	@JoinColumn(name = "owner_id")
	private Owner owner = null; //null = orphan by default
	
	//In order to use PetJPARepository for Pet entity, the default constructor (no arg) is required. 
	//Otherwise when invoking PetJPARepository, it may cause an error like this "org.hibernate.InstantiationException: No default constructor for entity"
	//Spring Data JPA requires a default constructor for each Entity
	public Pet() {
	}

	public Pet(String name, String type) {
		this.name = name;
		this.type = type;
	}
	public Pet(String name, String type, Owner owner) {
		this.name = name;
		this.type = type;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}
	
	
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return String.format("Pet[id=%d, name='%s', type='%s']", id, name, type);
	}

}
