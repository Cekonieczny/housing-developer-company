package com.capgemini;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.domain.QCustomerEntity;
import com.capgemini.domain.QFlatEntity;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.searchcriteria.FlatSizeSearchCriteria;
import com.capgemini.types.CustomerTO.CustomerTOBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
// @SpringBootTest(properties = "spring.profiles.active=mysql")
public class QueriesTest {

	@PersistenceContext
	EntityManager em;

	@Test
	public void shouldExecuteQuerySumOfFlatPricesBoughtByGivenCustomer() {
		// given
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QCustomerEntity qcustomer = QCustomerEntity.customerEntity;
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
		Integer sum = qf.select(qflat.price.sum()).from(qcustomer).join(qcustomer.flatEntities, qflat)
				.on(qflat.flatStatus.eq(FlatStatus.SOLD)).where(qcustomer.id.eq(customerId)).fetchOne();
		int sumActual = sum;
		// then
		Assert.assertEquals(sumActual,sumExpected);
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
		FlatSizeSearchCriteria searchCriteria = new FlatSizeSearchCriteria();
		searchCriteria.setFloorArea(searchedFloorArea);
		searchCriteria.setNumberOfBalconies(searchedNumberOfBalconies);
		searchCriteria.setNumberOfRooms(searchedNumberOfRooms);
		List<FlatEntity> listOfFlats = findNotSoldFlatsBySizeCriteria(searchCriteria);
		
		// then
		assertEquals(selectedFlatEntity,listOfFlats.iterator().next());
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
		FlatSizeSearchCriteria searchCriteria = new FlatSizeSearchCriteria();
		List<FlatEntity> listOfFlats = findNotSoldFlatsBySizeCriteria(searchCriteria);
		
		// then
		assertEquals(3,listOfFlats.size());
		Assert.assertTrue(listOfFlats.contains(selectedFlatEntity1));
		Assert.assertTrue(listOfFlats.contains(selectedFlatEntity2));
		Assert.assertTrue(listOfFlats.contains(selectedFlatEntity3));
	}
	
	@Test
	public void shouldExecuteQueryFindNotSoldFlatsByGivenCriteriaWithOneCriteria() {
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
		FlatSizeSearchCriteria searchCriteria = new FlatSizeSearchCriteria();
		searchCriteria.setFloorArea(searchedFloorArea);
		List<FlatEntity> listOfFlats = findNotSoldFlatsBySizeCriteria(searchCriteria);
		
		// then
		assertEquals(listOfFlats.iterator().next(),selectedFlatEntity);
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
		return CustomerMapper.toCustomerEntity(new CustomerTOBuilder().withAddress(address).withBirthDate(testDate)
				.withCreditCardNumber("123456789123456").withEmail("testemail@email.com").withName(testName)
				.withPhoneNumber("+48123456789").build());
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

		return flatEntity;
	}

	private void save20CustomersToMemoryDB() {
		for (int i = 0; i < 20; i++) {
			em.persist(getCustomer());
		}
	}

	private void save20FlatsToMemoryDB() {
		for (int i = 0; i < 20; i++) {
			em.persist(getCustomer());
		}
	}

	private void save20PurchaseOrdersToMemoryDB() {
		for (int i = 0; i < 20; i++) {
			save1PurchaseOrderToMemoryDB();
		}
	}

	private void save20ReservedOrdersToMemoryDB() {
		for (int i = 0; i < 20; i++) {
			save1ReservedOrderToMemoryDB();
		}
	}

	private void save1PurchaseOrderToMemoryDB() {
		CustomerEntity customerEntity = getCustomer();
		FlatEntity flatEntity = getFlat();
		em.persist(customerEntity);
		em.persist(flatEntity);
		flatEntity.setFlatStatus(FlatStatus.SOLD);
		flatEntity.addCustomerEntity(customerEntity);
	}

	private void save1ReservedOrderToMemoryDB() {

		CustomerEntity customerEntity = getCustomer();
		FlatEntity flatEntity = getFlat();
		em.persist(customerEntity);
		em.persist(flatEntity);
		flatEntity.setFlatStatus(FlatStatus.RESERVED);
		flatEntity.addCustomerEntity(customerEntity);

	}
	
	private List<FlatEntity> findNotSoldFlatsBySizeCriteria(FlatSizeSearchCriteria flatSizeSearchCriteria){
		JPAQuery<FlatEntity> q = new JPAQuery<FlatEntity>(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		q.from(qflat).select(qflat).where(qflat.flatStatus.ne(FlatStatus.SOLD));
		if (flatSizeSearchCriteria.getFloorArea() != null) {
			q.where(qflat.floorArea.eq(flatSizeSearchCriteria.getFloorArea()));
		}
		if (flatSizeSearchCriteria.getNumberOfBalconies() != null) {
			q.where(qflat.numberOfBalconies.eq(flatSizeSearchCriteria.getNumberOfBalconies()));
		}
		if (flatSizeSearchCriteria.getNumberOfRooms() != null) {
			q.where(qflat.numberOfRooms.eq(flatSizeSearchCriteria.getNumberOfRooms()));
		}
		return  q.fetch();
		
	}

}
