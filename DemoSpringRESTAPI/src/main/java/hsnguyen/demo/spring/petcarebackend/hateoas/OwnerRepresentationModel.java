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
//@Relation(collectionRelation = "owners") is used to change the default name "ownerRepresentationModelList" in the result returned by CollectionModel.of(List<OwnerRepresentationModel>, ...)
//invoked in getAllOwners() of the controller class
 @Relation(collectionRelation = "owners")
public class OwnerRepresentationModel extends RepresentationModel<OwnerRepresentationModel> {
	private Integer id;
	private String name;
	private String contactInfo;

	public OwnerRepresentationModel(Integer id, String name, String contactInfo) {
		super();
		this.id = id;
		this.name = name;
		this.contactInfo = contactInfo;
	}
	

	public Integer getId() {
		return id;
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

}
