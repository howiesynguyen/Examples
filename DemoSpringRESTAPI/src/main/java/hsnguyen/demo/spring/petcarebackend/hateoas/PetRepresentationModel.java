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

package hsnguyen.demo.spring.petcarebackend.hateoas;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

//RepresentationModel is used as a base class for DTOs to collect links
//@Relation(collectionRelation = "pets") is used to change the default name "petRepresentationModelList" in the result returned by CollectionModel.of(List<PetRepresentationModel>, ...)
@Relation(collectionRelation = "pets")
public class PetRepresentationModel extends RepresentationModel<PetRepresentationModel> {
	private Integer id;
	private String name;
	private String type;
	private Integer ownerId;

	public PetRepresentationModel(Integer id, String name, String type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public PetRepresentationModel(Integer id, String name, String type, Integer ownerId) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.ownerId = ownerId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}
}
