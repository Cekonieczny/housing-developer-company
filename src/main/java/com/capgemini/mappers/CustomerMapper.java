package com.capgemini.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.CustomerTO.CustomerTOBuilder;

public class CustomerMapper {
	
	@PersistenceContext
	private static EntityManager em;
	
	public static CustomerTO toCustomerTO(CustomerEntity customerEntity) {
		if (customerEntity == null)
			return null;
		
		Set<Long> flatIds = FlatMapper.map2Ids(customerEntity.getFlatEntities());

		return new CustomerTOBuilder()
				.withAddress(customerEntity.getAddress())
				.withBirthDate(customerEntity.getBirthDate())
				.withCreatedOn(customerEntity.getCreatedOn())
				.withCreditCardNumber(customerEntity.getCreditCardNumber())
				.withId(customerEntity.getId())
				.withName(customerEntity.getName())
				.withEmail(customerEntity.getEmail())
				.withPhoneNumber(customerEntity.getPhoneNumber())
				.withUpdatedOn(customerEntity.getUpdatedOn())
				.withFlatIds(flatIds).build();
	}

	public static CustomerEntity toCustomerEntity(CustomerTO customerTO) {
		if (customerTO == null)
			return null;
		
		Set<FlatEntity> flatEntities = FlatMapper.map2Entities(customerTO.getFlatIds());

		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setAddress(customerTO.getAddress());
		customerEntity.setBirthDate(customerTO.getBirthDate());
		customerEntity.setCreatedOn(customerTO.getCreatedOn());
		customerEntity.setCreditCardNumber(customerTO.getCreditCardNumber());
		customerEntity.setEmail(customerTO.getEmail());
		customerEntity.setId(customerTO.getId());
		customerEntity.setName(customerTO.getName());
		customerEntity.setPhoneNumber(customerTO.getPhoneNumber());
		customerEntity.setUpdatedOn(customerTO.getUpdatedOn());
		customerEntity.setCreatedOn(customerTO.getUpdatedOn());
		customerEntity.setFlatEntities(flatEntities);
		
		return customerEntity;
	}

	public static Set<Long> map2Ids(Set<CustomerEntity> customerEntities) {
		return customerEntities.stream().map(CustomerEntity::getId).collect(Collectors.toSet());
	}
	
	public static Set<CustomerTO> map2TOs(Set<CustomerEntity> customerEntities) { 
		return customerEntities.stream().map(CustomerMapper::toCustomerTO).collect(Collectors.toSet());
	}


	public static Set<CustomerEntity> map2Entities(Set<Long> customerIds) {
		TypedQuery<CustomerEntity> q = em.createQuery("SELECT c FROM CustomerEntity WHERE c.id in :customerIds",
				CustomerEntity.class);
		q.setParameter("customerIds", customerIds);
		return q.getResultList().stream().collect(Collectors.toSet());
	}
}
