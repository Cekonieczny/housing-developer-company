package com.capgemini.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.FlatDao;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.mappers.BuildingMapper;
import com.capgemini.mappers.FlatMapper;
import com.capgemini.types.FlatTO;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
// @SpringBootTest(properties = "spring.profiles.active=mysql")
public class FlatServiceTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private FlatService flatService;

	@Autowired
	private FlatMapper fm;

	@Autowired
	private BuildingMapper bm;

	
	@Test
	public void shouldSaveFlat() {

		// given
		FlatEntity testFlat = getFlat();
		FlatTO testFlatTO = fm.toFlatTO(testFlat);

		// when
		FlatTO savedFlat = flatService.saveOrUpdate(testFlatTO);
		FlatEntity flatEntity = em.find(FlatEntity.class, savedFlat.getId());
		FlatTO selectedFlat = fm.toFlatTO(flatEntity);

		// then
		assertNotNull(savedFlat);
		assertTrue(selectedFlat.equals(savedFlat));
	}


	@Test
	public void shouldFindFlat() {

		// given
		FlatEntity flatEntity = getFlat();
		em.persist(flatEntity);
		// when
		FlatTO savedFlat = flatService.findOne(flatEntity.getId());

		// then
		assertNotNull(savedFlat);
		assertEquals(fm.toFlatTO(flatEntity), savedFlat);
	}

	
	@Test
	public void shouldDeleteFlat() {

		// given
		FlatEntity flatEntity = getFlat();
		em.persist(flatEntity);
		// when
		flatService.delete(flatEntity.getId());
		FlatEntity deletedFlatEntity = em.find(FlatEntity.class, flatEntity.getId());

		// then
		assertNull(deletedFlatEntity);
	}

	
	@Test
	public void shouldAddFlatToBuilding() {
		// given
		FlatEntity flatEntity = getFlat();
		em.persist(flatEntity);
		BuildingEntity buildingEntity = getBuilding();
		em.persist(buildingEntity);
		// when
		FlatTO resultFlatTO = flatService.addFlatToBuilding(flatEntity.getId(), buildingEntity.getId());
		// then
		assertEquals(bm.toBuildingTO(buildingEntity), resultFlatTO.getBuildingTO());
		assertEquals(fm.toFlatTO(flatEntity), resultFlatTO);
	}


	@Test
	public void shouldRemoveFlatFromBuilding() {
		// given
		FlatEntity flatEntity = getFlat();
		em.persist(flatEntity);
		BuildingEntity buildingEntity = getBuilding();
		em.persist(buildingEntity);
		buildingEntity.addFlatEntity(flatEntity);
		// when
		FlatTO resultFlatTO = flatService.removeFlatFromBuilding(flatEntity.getId(), buildingEntity.getId());
		// then
		assertNull(resultFlatTO.getBuildingTO());
		assertNull(flatEntity.getBuildingEntity());
		assertTrue(buildingEntity.getFlatEntities().isEmpty());
	}

	
	@Test(expected = OptimisticLockException.class)
	public void shouldThrowOptimisticLockException() {
		FlatEntity flatEntity = getFlat();
		em.persist(flatEntity);
		em.flush();
		em.detach(flatEntity);

		final FlatEntity newFlatEntity = em.find(FlatEntity.class, flatEntity.getId());
		newFlatEntity.setFlatNumber("34");
		em.persist(newFlatEntity);
		em.flush();

		flatEntity.setFlatNumber("25");
		flatEntity = em.merge(flatEntity);
		em.persist(flatEntity);
	}
	

	private CustomerEntity getCustomer() {
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
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setAddress(address);
		customerEntity.setBirthDate(testDate);
		customerEntity.setCreditCardNumber("123456789123456");
		customerEntity.setEmail("testemail@email.com");
		customerEntity.setName(testName);
		customerEntity.setPhoneNumber("+48123456789");
		return customerEntity;
	}

	private BuildingEntity getBuilding() {
		Location location = new Location();
		location.setBuildingNumber("10");
		location.setPlaceName("placename");
		location.setStreetName("streetname");
		location.setZipCode("zipcode");
		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setHasLift(true);
		buildingEntity.setLocation(location);
		buildingEntity.setNumberOfFlats(11);
		buildingEntity.setNumberOfFloors(12);
		buildingEntity.setDescription("description");
		return buildingEntity;
	}

	private FlatEntity getFlat() {
		String flatNumber = "11";
		int floorArea = 40;
		int numberOfBalconies = 2;
		int numberOfRooms = 3;
		int price = 200000;

		BuildingEntity buildingEntity = getBuilding();
		em.persist(buildingEntity);

		FlatEntity flatEntity = new FlatEntity();
		flatEntity.setBuildingEntity(buildingEntity);
		flatEntity.setCustomerEntities(new HashSet<>());
		flatEntity.setFlatNumber(flatNumber);
		flatEntity.setFloorArea(floorArea);
		flatEntity.setNumberOfBalconies(numberOfBalconies);
		flatEntity.setNumberOfRooms(numberOfRooms);
		flatEntity.setPrice(price);
		flatEntity.setFloorNumber("3");

		return flatEntity;
	}

}
