package com.capgemini.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.domain.CustomerEntity;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.CustomerTO.CustomerTOBuilder;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
// @SpringBootTest(properties = "spring.profiles.active=mysql")
public class CustomerServiceTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	CustomerMapper cm;

	@Test
	public void shouldSaveCustomer() {

		// given
		CustomerTO testCustomer = getTestCustomerTO();
		// when
		CustomerTO savedCustomer = customerService.saveOrUpdate(testCustomer);
		CustomerEntity customerEntity = em.find(CustomerEntity.class, savedCustomer.getId());
		CustomerTO selectedCustomer = cm.toCustomerTO(customerEntity);

		// then
		assertNotNull(savedCustomer);
		assertTrue(selectedCustomer.equals(savedCustomer));
	}
	
	@Test
	public void shouldFindCustomer() {

		// given
		CustomerEntity customerEntity = cm.toCustomerEntity(getTestCustomerTO());
		em.persist(customerEntity);
		// when
		CustomerTO savedCustomer = customerService.findOne(customerEntity.getId());

		// then
		assertNotNull(savedCustomer);
		assertEquals(cm.toCustomerTO(customerEntity),savedCustomer);
	}
	
	@Test
	public void shouldDeleteCustomer() {

		// given
		CustomerEntity customerEntity = cm.toCustomerEntity(getTestCustomerTO());
		em.persist(customerEntity);
		// when
		customerService.delete(customerEntity.getId());
		CustomerEntity deletedCustomerEntity = em.find(CustomerEntity.class, customerEntity.getId());

		// then
		assertNull(deletedCustomerEntity);
	}
	
	

	private CustomerTO getTestCustomerTO() {
		Location location = new Location();
		location.setBuildingNumber("11");
		location.setPlaceName("Poznań");
		location.setStreetName("Warszawska");
		location.setZipCode("22-333");
		Address address = new Address();
		address.setLocation(location);
		address.setApartmentNumber("22");
		Date testDate = new Date();
		Name testName = new Name();
		testName.setFirstName("firstname");
		testName.setLastName("lastname");
		return new CustomerTOBuilder().withAddress(address).withBirthDate(testDate)
				.withCreditCardNumber("123456789123456").withEmail("testemail@email.com").withFlatIds(new HashSet<>())
				.withId(1L).withName(testName).withPhoneNumber("+48123456789").build();
	}

}
