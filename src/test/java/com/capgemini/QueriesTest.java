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
import com.capgemini.domain.QBuildingEntity;
import com.capgemini.domain.QCustomerEntity;
import com.capgemini.domain.QFlatEntity;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.searchcriteria.FlatSizeSearchCriteria;
import com.querydsl.jpa.JPAExpressions;
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
		List<FlatEntity> listOfFlats = findNotSoldFlatsBySizeCriteria(searchCriteriaFrom, searchCriteriaTo);

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
		List<FlatEntity> listOfFlats = findNotSoldFlatsBySizeCriteria(searchCriteriaFrom, searchCriteriaTo);

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
		List<FlatEntity> listOfFlats = findNotSoldFlatsBySizeCriteria(searchCriteriaFrom, searchCriteriaTo);

		// then
		assertEquals(listOfFlats.iterator().next(), selectedFlatEntity);
	}

	@Test
	public void shouldExecuteQueryFindAvgFlatPriceInACertainBuilding() {
		// given
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
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
		Double avg = qf.select(qflat.price.avg()).from(qbuilding).join(qbuilding.flatEntities, qflat)
				.on(qbuilding.id.eq(buildingId)).fetchOne();
		// then
		Assert.assertEquals(avgExpected, avg);
	}

	@Test
	public void shouldExecuteQueryFindCustomersWhoBoughtMoreThanOneFlat() {
		// given
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QCustomerEntity qcustomer = QCustomerEntity.customerEntity;
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
		customer1.addFlatEntity(flatEntity1);
		customer1.addFlatEntity(flatEntity2);
		em.persist(customer2);

		for (int i = 0; i < 2; i++) {
			CustomerEntity customer = getCustomer();
			customer.addFlatEntity(flatEntity1);
			em.persist(customer);
		}

		Long minFlatCount = 1L;

		// when
		List<CustomerEntity> listOfCustomers = qf.selectFrom(qcustomer).join(qcustomer.flatEntities, qflat)
				.on(qflat.flatStatus.eq(FlatStatus.SOLD)).where(qcustomer.flatEntities.size().gt(minFlatCount)).fetch();
		// then
		assertEquals(2, listOfCustomers.size());
	}
	
	@Test
	public void shouldExecuteQueryFindBuildingWithMostFlatsFree() {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
		BuildingEntity searchedBuilding = getBuilding();
		BuildingEntity buildingEntity = getBuilding();
		FlatEntity flatEntityFree1 = getFlat();
		FlatEntity flatEntityFree2 = getFlat();
		FlatEntity flatEntitySold = getFlat();
		flatEntitySold.setFlatStatus(FlatStatus.SOLD);
		FlatEntity flatEntityReserved = getFlat();
		flatEntitySold.setFlatStatus(FlatStatus.RESERVED);

		// create 1st purchase order
		em.persist(searchedBuilding);
		em.persist(buildingEntity);
		em.persist(flatEntityFree1);
		em.persist(flatEntityFree2);
		em.persist(flatEntitySold);
		em.persist(flatEntityReserved);
		searchedBuilding.addFlatEntity(flatEntityFree1);
		searchedBuilding.addFlatEntity(flatEntityFree2);
		buildingEntity.addFlatEntity(flatEntityFree1);
		buildingEntity.addFlatEntity(flatEntitySold);
		buildingEntity.addFlatEntity(flatEntityReserved);

		// when
		JPAQuery<Long> q1 = new JPAQuery<Long>(em);
		//List<Long> maxCount = q1.select(qflat.count()).from(qbuilding).join(qbuilding.flatEntities,qflat).on(qflat.flatStatus.eq(FlatStatus.FREE)).groupBy(qbuilding).orderBy(qflat.count().desc()).fetch();
		/*JPAExpressions.select(qflat.count()).from(qbuilding).join(qbuilding.flatEntities,qflat).on(qflat.flatStatus.eq(FlatStatus.FREE)).groupBy(qbuilding))*/
		BuildingEntity foundBuilding =  qf.select(qbuilding).join(qbuilding.flatEntities, qflat)
				.on(qflat.flatStatus.eq(FlatStatus.FREE)).where(qflat.count().eq(JPAExpressions.select(qflat.count().max()).from(qbuilding).join(qbuilding.flatEntities,qflat).on(qflat.flatStatus.eq(FlatStatus.FREE)).groupBy(qbuilding))).fetchOne();
		// then
		assertEquals(searchedBuilding, foundBuilding);
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
		Long count = findFlatCountOfGivenStatusInACertainBuilding(flatStatus, buildingId);
		// then
		Assert.assertEquals(countExpected, count);
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

	private List<FlatEntity> findNotSoldFlatsBySizeCriteria(FlatSizeSearchCriteria flatSizeSearchCriteriaFrom,
			FlatSizeSearchCriteria flatSizeSearchCriteriaTo) {
		JPAQuery<FlatEntity> q = new JPAQuery<FlatEntity>(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;

		q.from(qflat).select(qflat).where(qflat.flatStatus.ne(FlatStatus.SOLD));
		if (flatSizeSearchCriteriaFrom.getFloorArea() != null && flatSizeSearchCriteriaTo.getFloorArea() != null) {
			q.where(qflat.floorArea.between(flatSizeSearchCriteriaFrom.getFloorArea(),
					flatSizeSearchCriteriaTo.getFloorArea()));
		}
		if (flatSizeSearchCriteriaFrom.getNumberOfBalconies() != null
				&& flatSizeSearchCriteriaTo.getNumberOfBalconies() != null) {
			q.where(qflat.numberOfBalconies.between(flatSizeSearchCriteriaFrom.getNumberOfBalconies(),
					flatSizeSearchCriteriaTo.getNumberOfBalconies()));
		}
		if (flatSizeSearchCriteriaFrom.getNumberOfRooms() != null
				&& flatSizeSearchCriteriaTo.getNumberOfRooms() != null) {
			q.where(qflat.numberOfRooms.between(flatSizeSearchCriteriaFrom.getNumberOfRooms(),
					flatSizeSearchCriteriaTo.getNumberOfRooms()));
		}
		return q.fetch();

	}

	private Long findFlatCountOfGivenStatusInACertainBuilding(FlatStatus flatStatus, Long buildingId) {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
		return qf.select(qflat.count()).from(qbuilding).join(qbuilding.flatEntities, qflat)
				.on(qbuilding.id.eq(buildingId)).where(qflat.flatStatus.eq(flatStatus)).fetchOne();
	}

}
