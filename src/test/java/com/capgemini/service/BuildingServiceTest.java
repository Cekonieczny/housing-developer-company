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

import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.mappers.BuildingMapper;
import com.capgemini.types.BuildingTO;
import com.capgemini.types.CustomerTO;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
// @SpringBootTest(properties = "spring.profiles.active=mysql")
public class BuildingServiceTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private BuildingMapper bm;

	@Test
	public void shouldSaveBuilding() {

		// given
		BuildingEntity testBuilding = getBuilding();
		BuildingTO testBuildingTO = bm.toBuildingTO(testBuilding);

		// when
		BuildingTO savedBuilding = buildingService.saveOrUpdate(testBuildingTO);
		BuildingEntity buildingEntity = em.find(BuildingEntity.class, savedBuilding.getId());
		BuildingTO selectedBuilding = bm.toBuildingTO(buildingEntity);

		// then
		assertNotNull(savedBuilding);
		assertTrue(selectedBuilding.equals(savedBuilding));
	}

	@Test
	public void shouldFindBuilding() {

		// given
		BuildingEntity buildingEntity = getBuilding();
		em.persist(buildingEntity);
		// when
		BuildingTO savedBuilding = buildingService.findOne(buildingEntity.getId());

		// then
		assertNotNull(savedBuilding);
		assertEquals(bm.toBuildingTO(buildingEntity), savedBuilding);
	}

	@Test
	public void shouldDeleteBuilding() {

		// given
		BuildingEntity buildingEntity = getBuilding();
		em.persist(buildingEntity);
		// when
		buildingService.delete(buildingEntity.getId());
		BuildingEntity deletedBuildingEntity = em.find(BuildingEntity.class, buildingEntity.getId());

		// then
		assertNull(deletedBuildingEntity);
	}

	@Test
	public void shouldRemoveAllFlatsAttachedToBuildingByCascadeTypeRemove() {
		// given
		BuildingEntity buildingEntity = getBuilding();
		em.persist(buildingEntity);
		FlatEntity flatEntity1 = getFlat();
		FlatEntity flatEntity2 = getFlat();
		FlatEntity flatEntity3 = getFlat();
		em.persist(flatEntity1);
		em.persist(flatEntity2);
		em.persist(flatEntity3);
		buildingEntity.addFlatEntity(flatEntity1);
		buildingEntity.addFlatEntity(flatEntity2);
		buildingEntity.addFlatEntity(flatEntity3);
		// when
		buildingService.delete(buildingEntity.getId());
		BuildingEntity deletedBuildingEntity = em.find(BuildingEntity.class, buildingEntity.getId());
		assertNull(deletedBuildingEntity);

		// then
		assertNull(em.find(FlatEntity.class, flatEntity1.getId()));
		assertNull(em.find(FlatEntity.class, flatEntity2.getId()));
		assertNull(em.find(FlatEntity.class, flatEntity3.getId()));
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
