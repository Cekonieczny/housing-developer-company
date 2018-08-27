package com.capgemini;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.BuildingDao;
import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.FlatDao;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.searchcriteria.FlatSizeSearchCriteria;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
// @SpringBootTest(properties = "spring.profiles.active=mysql")
public class QueriesTest {

	@PersistenceContext
	EntityManager em;

	@Autowired
	CustomerMapper cm;

	@Autowired
	FlatDao flatRepository;

	@Autowired
	BuildingDao buildingRepository;

	@Autowired
	CustomerDao customerRepository;

	@Test
	public void shouldExecuteQuerySumOfFlatPricesBoughtByGivenCustomer() {
		// given
		CustomerEntity customerEntity = getCustomer();
		FlatEntity flatEntitySold = getFlat();

		// create 1st purchase order
		em.persist(customerEntity);
		em.persist(flatEntitySold);
		flatEntitySold.setFlatStatus(FlatStatus.SOLD);
		flatEntitySold.addCustomerEntity(customerEntity);
		// create 1st reserved order
		FlatEntity flatEntityReserved = getFlat();
		em.persist(flatEntitySold);
		flatEntityReserved.setFlatStatus(FlatStatus.RESERVED);
		flatEntityReserved.addCustomerEntity(customerEntity);
		// create 2nd purchase order
		FlatEntity flatEntitySold2 = getFlat();
		em.persist(flatEntitySold2);
		flatEntitySold2.setFlatStatus(FlatStatus.SOLD);
		flatEntitySold2.addCustomerEntity(customerEntity);

		Long customerId = customerEntity.getId();

		int sumExpected = flatEntitySold.getPrice() + flatEntitySold2.getPrice();

		// when
		Integer sum = customerRepository.findSumOfFlatPricesBoughtByGivenCustomer(customerId);
		int sumActual = sum;
		// then
		Assert.assertEquals(sumActual, sumExpected);
	}

	@Test
	public void shouldExecuteQueryFindNotSoldFlatsByGivenCriteria() {
		// given
		int searchedFloorArea = 65;
		int searchedNumberOfBalconies = 5;
		int searchedNumberOfRooms = 4;
		// create 2 free status flats
		em.persist(getFlat());
		em.persist(getFlat());
		// create reserved flat with given criteria
		FlatEntity selectedFlatEntity = getFlat();
		em.persist(selectedFlatEntity);
		selectedFlatEntity.setFlatStatus(FlatStatus.RESERVED);
		selectedFlatEntity.setFloorArea(searchedFloorArea);
		selectedFlatEntity.setNumberOfBalconies(searchedNumberOfBalconies);
		selectedFlatEntity.setNumberOfRooms(searchedNumberOfRooms);

		// when
		FlatSizeSearchCriteria searchCriteriaFrom = new FlatSizeSearchCriteria();
		searchCriteriaFrom.setFloorArea(searchedFloorArea);
		searchCriteriaFrom.setNumberOfBalconies(searchedNumberOfBalconies);
		searchCriteriaFrom.setNumberOfRooms(searchedNumberOfRooms);
		FlatSizeSearchCriteria searchCriteriaTo = new FlatSizeSearchCriteria();
		searchCriteriaTo.setFloorArea(searchedFloorArea);
		searchCriteriaTo.setNumberOfBalconies(searchedNumberOfBalconies);
		searchCriteriaTo.setNumberOfRooms(searchedNumberOfRooms);

		List<FlatEntity> listOfFlats = flatRepository.findNotSoldFlatsBySizeCriteria(searchCriteriaFrom,
				searchCriteriaTo);

		// then
		assertEquals(selectedFlatEntity, listOfFlats.iterator().next());
	}

	@Test
	public void shouldExecuteQueryFindNotSoldFlatsByGivenCriteriaWithNullCriteria() {
		// given
		int searchedFloorArea = 65;
		int searchedNumberOfBalconies = 5;
		int searchedNumberOfRooms = 4;
		// create 2 free status flats
		FlatEntity selectedFlatEntity1 = getFlat();
		em.persist(selectedFlatEntity1);
		FlatEntity selectedFlatEntity2 = getFlat();
		em.persist(selectedFlatEntity2);
		// create reserved flat with given criteria
		FlatEntity selectedFlatEntity3 = getFlat();
		em.persist(selectedFlatEntity3);
		selectedFlatEntity3.setFlatStatus(FlatStatus.RESERVED);
		selectedFlatEntity3.setFloorArea(searchedFloorArea);
		selectedFlatEntity3.setNumberOfBalconies(searchedNumberOfBalconies);
		selectedFlatEntity3.setNumberOfRooms(searchedNumberOfRooms);

		// when
		FlatSizeSearchCriteria searchCriteriaFrom = new FlatSizeSearchCriteria();
		FlatSizeSearchCriteria searchCriteriaTo = new FlatSizeSearchCriteria();
		List<FlatEntity> listOfFlats = flatRepository.findNotSoldFlatsBySizeCriteria(searchCriteriaFrom,
				searchCriteriaTo);

		// then
		assertEquals(3, listOfFlats.size());
		Assert.assertTrue(listOfFlats.contains(selectedFlatEntity1));
		Assert.assertTrue(listOfFlats.contains(selectedFlatEntity2));
		Assert.assertTrue(listOfFlats.contains(selectedFlatEntity3));
	}

	@Test
	public void shouldExecuteQueryFindNotSoldFlatsByGivenCriteriaWithFloorAreaOnly() {
		// given
		int searchedFloorArea = 65;
		// create 2 free status flats
		em.persist(getFlat());
		em.persist(getFlat());
		// create reserved flat with given criteria
		FlatEntity selectedFlatEntity = getFlat();
		em.persist(selectedFlatEntity);
		selectedFlatEntity.setFlatStatus(FlatStatus.RESERVED);
		selectedFlatEntity.setFloorArea(searchedFloorArea);

		// when
		FlatSizeSearchCriteria searchCriteriaFrom = new FlatSizeSearchCriteria();
		searchCriteriaFrom.setFloorArea(searchedFloorArea);
		FlatSizeSearchCriteria searchCriteriaTo = new FlatSizeSearchCriteria();
		searchCriteriaTo.setFloorArea(searchedFloorArea);
		List<FlatEntity> listOfFlats = flatRepository.findNotSoldFlatsBySizeCriteria(searchCriteriaFrom,
				searchCriteriaTo);

		// then
		assertEquals(listOfFlats.iterator().next(), selectedFlatEntity);
	}

	@Test
	public void shouldExecuteQueryFindAvgFlatPriceInACertainBuilding() {
		// given
		BuildingEntity buildingEntity = getBuilding();
		FlatEntity flatEntity1 = getFlat();
		FlatEntity flatEntity2 = getFlat();

		// create 1st purchase order
		em.persist(buildingEntity);
		em.persist(flatEntity1);
		flatEntity1.setPrice(3000000);
		em.persist(flatEntity2);
		flatEntity2.setPrice(4000000);
		buildingEntity.addFlatEntity(flatEntity1);
		buildingEntity.addFlatEntity(flatEntity2);

		Long buildingId = buildingEntity.getId();

		Double avgExpected = (double) ((flatEntity1.getPrice() + flatEntity2.getPrice()) / 2);

		// when
		Double avg = buildingRepository.findAvgFlatPriceInACertainBuilding(buildingId);
		// then
		Assert.assertEquals(avgExpected, avg);
	}

	@Test
	public void shouldExecuteQueryFindCustomersWhoBoughtMoreThanOneFlat() {
		// given
		FlatEntity flatEntity1 = getFlat();
		flatEntity1.setFlatStatus(FlatStatus.SOLD);
		FlatEntity flatEntity2 = getFlat();
		flatEntity2.setFlatStatus(FlatStatus.SOLD);
		em.persist(flatEntity1);
		em.persist(flatEntity2);

		CustomerEntity customer1 = getCustomer();
		customer1.addFlatEntity(flatEntity1);
		customer1.addFlatEntity(flatEntity2);
		em.persist(customer1);
		CustomerEntity customer2 = getCustomer();
		customer2.addFlatEntity(flatEntity1);
		customer2.addFlatEntity(flatEntity2);
		em.persist(customer2);

		CustomerEntity customer3 = getCustomer();
		customer3.addFlatEntity(flatEntity1);
		em.persist(customer3);

		CustomerEntity customer4 = getCustomer();
		customer4.addFlatEntity(flatEntity1);
		em.persist(customer4);

		// when
		Set<CustomerEntity> setOfCustomers = customerRepository.findCustomersWhoBoughtMoreThanOneFlat();
		// then
		assertEquals(2, setOfCustomers.size());
	}

	@Test
	public void shouldExecuteQueryFindBuildingWithMostQuantityOfFlatsFree() {
		BuildingEntity searchedBuilding = getBuilding();
		BuildingEntity buildingEntity = getBuilding();
		FlatEntity flatEntityFree1 = getFlat();
		FlatEntity flatEntityFree2 = getFlat();
		FlatEntity flatEntityFree3 = getFlat();
		FlatEntity flatEntitySold = getFlat();
		flatEntitySold.setFlatStatus(FlatStatus.SOLD);
		FlatEntity flatEntityReserved = getFlat();
		flatEntityReserved.setFlatStatus(FlatStatus.RESERVED);

		em.persist(searchedBuilding);
		em.persist(buildingEntity);
		em.persist(flatEntityFree1);
		em.persist(flatEntityFree2);
		em.persist(flatEntityFree3);
		em.persist(flatEntitySold);
		em.persist(flatEntityReserved);
		searchedBuilding.addFlatEntity(flatEntityFree1);
		searchedBuilding.addFlatEntity(flatEntityFree2);
		buildingEntity.addFlatEntity(flatEntityFree3);
		buildingEntity.addFlatEntity(flatEntitySold);
		buildingEntity.addFlatEntity(flatEntityReserved);
		// when
		List<BuildingEntity> foundBuildings = buildingRepository.findBuildingWithMostQuantityOfFlatsFree();
		// then
		assertEquals(1, foundBuildings.size());
		assertEquals(searchedBuilding, foundBuildings.get(0));

	}

	@Test
	public void shouldExecuteQueryFindFlatCountOfGivenStatusInACertainBuilding() {
		// given
		BuildingEntity buildingEntity = getBuilding();
		FlatEntity flatEntity1 = getFlat();
		FlatEntity flatEntity2 = getFlat();

		em.persist(buildingEntity);
		em.persist(flatEntity1);
		em.persist(flatEntity2);
		buildingEntity.addFlatEntity(flatEntity1);
		buildingEntity.addFlatEntity(flatEntity2);

		Long buildingId = buildingEntity.getId();

		FlatStatus flatStatus = FlatStatus.FREE;

		Long countExpected = 2L;

		// when
		Long count = flatRepository.findFlatCountOfGivenStatusInACertainBuilding(flatStatus, buildingId);
		// then
		Assert.assertEquals(countExpected, count);
	}

	@Test
	public void shouldExecuteQueryFindFlatsSuitedForHandicapped() {
		// given
		BuildingEntity buildingWithLift = getBuilding();
		buildingWithLift.setHasLift(true);
		BuildingEntity buildingWithNoLift = getBuilding();
		buildingWithNoLift.setHasLift(false);
		FlatEntity flatOnFloor1WithLift = getFlat();
		flatOnFloor1WithLift.setFloorNumber("1");
		FlatEntity flatOnFloor0WithLift = getFlat();
		flatOnFloor0WithLift.setFloorNumber("0");
		FlatEntity flatOnFloor0WithNoLift = getFlat();
		flatOnFloor0WithNoLift.setFloorNumber("0");
		FlatEntity flatOnFloor3WithNoLift = getFlat();
		flatOnFloor3WithNoLift.setFloorNumber("3");

		// create 1st purchase order
		em.persist(buildingWithNoLift);
		em.persist(buildingWithLift);
		em.persist(flatOnFloor1WithLift);
		em.persist(flatOnFloor0WithLift);
		em.persist(flatOnFloor0WithNoLift);
		em.persist(flatOnFloor3WithNoLift);
		buildingWithLift.addFlatEntity(flatOnFloor1WithLift);
		buildingWithLift.addFlatEntity(flatOnFloor0WithLift);
		buildingWithNoLift.addFlatEntity(flatOnFloor0WithNoLift);
		buildingWithNoLift.addFlatEntity(flatOnFloor3WithNoLift);

		// when
		List<FlatEntity> listOfFlats = flatRepository.findFlatsSuitedForHandicapped();
		// then
		assertEquals(3, listOfFlats.size());
		assertTrue(listOfFlats.contains(flatOnFloor1WithLift));
		assertTrue(listOfFlats.contains(flatOnFloor0WithLift));
		assertTrue(listOfFlats.contains(flatOnFloor0WithNoLift));
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
