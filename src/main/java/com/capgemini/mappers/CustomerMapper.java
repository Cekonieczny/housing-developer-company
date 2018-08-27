package com.capgemini.mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.CustomerTO.CustomerTOBuilder;

@Service
public class CustomerMapper {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private FlatMapper flatMapper;
	
	public  CustomerTO toCustomerTO(CustomerEntity customerEntity) {
		if (customerEntity == null)
			return null;
		
		Set<Long> flatIds = flatMapper.map2Ids(customerEntity.getFlatEntities());

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

	public CustomerEntity toCustomerEntity(CustomerTO customerTO) {
		if (customerTO == null)
			return null;
		
		Set<FlatEntity> flatEntities = flatMapper.map2Entities(customerTO.getFlatIds()).stream().collect(Collectors.toSet());

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

	public  Set<Long> map2Ids(Set<CustomerEntity> customerEntities) {
		return customerEntities.stream().map(CustomerEntity::getId).collect(Collectors.toSet());
	}
	
	public  Set<CustomerTO> map2TOs(Set<CustomerEntity> customerEntities) { 
		return customerEntities.stream().map(this::toCustomerTO).collect(Collectors.toSet());
	}


	public Set<CustomerEntity> map2Entities(Set<Long> customerIds) {
		if(customerIds.isEmpty()){
			return new HashSet<>();
		}
		TypedQuery<CustomerEntity> q = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.id in :customerIds",
				CustomerEntity.class);
		q.setParameter("customerIds", customerIds);
		return q.getResultList().stream().collect(Collectors.toSet());
	}
}
