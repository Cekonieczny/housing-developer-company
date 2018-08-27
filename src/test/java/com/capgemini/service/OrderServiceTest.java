package com.capgemini.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.embeddable.Address;
import com.capgemini.embeddable.Location;
import com.capgemini.embeddable.Name;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.mappers.FlatMapper;
import com.capgemini.service.exceptions.InvalidOrderPlacedException;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.FlatTO;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=hsql")
// @SpringBootTest(properties = "spring.profiles.active=mysql")
public class OrderServiceTest {

	@PersistenceContext
	EntityManager em;

	@Autowired
	OrderService orderService;
	@Autowired
	CustomerMapper cm;
	@Autowired
	FlatMapper fm;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void shouldFindCustomersByPlacedOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		FlatEntity flatEntity2 = getFlat();
		em.persist(flatEntity2);
		CustomerEntity customerEntity1 = getCustomer();
		CustomerEntity customerEntity2 = getCustomer();
		CustomerEntity customerEntity3 = getCustomer();
		CustomerEntity customerEntity4 = getCustomer();
		em.persist(customerEntity1);
		em.persist(customerEntity2);
		em.persist(customerEntity3);
		em.persist(customerEntity4);
		flatEntity1.addCustomerEntity(customerEntity1);
		flatEntity1.addCustomerEntity(customerEntity2);
		flatEntity1.addCustomerEntity(customerEntity3);
		flatEntity2.addCustomerEntity(customerEntity4);
		// when
		Set<CustomerTO> customersWhoPlacedOrder = orderService.findCustomersByPlacedOrder(flatEntity1.getId());
		CustomerTO customerTO1 = cm.toCustomerTO(customerEntity1);
		customerTO1.toString();
		// then
		assertTrue(customersWhoPlacedOrder.contains(cm.toCustomerTO(customerEntity1)));
		assertTrue(customersWhoPlacedOrder.contains(cm.toCustomerTO(customerEntity2)));
		assertTrue(customersWhoPlacedOrder.contains(cm.toCustomerTO(customerEntity3)));
	}

	@Test
	public void shouldCreateReservationOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		// when
		FlatTO flatTO = orderService.createReservationOrder(flatEntity1.getId(), customerEntity1.getId());
		// then
		assertTrue(flatTO.getCustomerIds().contains(customerEntity1.getId()));
		assertEquals(FlatStatus.RESERVED, flatTO.getFlatStatus());
	}

	@Test()
	public void shouldThrowExceptionOnCreateReservationOrderByCustomerWithMoreThan3Reservations() {
		// given
		FlatEntity flatEntity1 = getFlat();
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		FlatEntity flatEntity2 = getFlat();
		flatEntity2.setFlatStatus(FlatStatus.RESERVED);
		FlatEntity flatEntity3 = getFlat();
		flatEntity3.setFlatStatus(FlatStatus.RESERVED);
		FlatEntity flatEntity4 = getFlat();
		flatEntity4.setFlatStatus(FlatStatus.RESERVED);
		FlatEntity flatEntity5 = getFlat();
		em.persist(flatEntity1);
		em.persist(flatEntity2);
		em.persist(flatEntity3);
		em.persist(flatEntity4);
		em.persist(flatEntity5);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		customerEntity1.addFlatEntity(flatEntity1);
		customerEntity1.addFlatEntity(flatEntity2);
		customerEntity1.addFlatEntity(flatEntity3);
		customerEntity1.addFlatEntity(flatEntity4);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("This customer has already reserved 3 flats on his/her own");
		orderService.createReservationOrder(flatEntity5.getId(), customerEntity1.getId());
	}

	@Test()
	public void shouldThrowExceptionOnCreateReservationOrderWithFlatAlreadyAssignedToOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		em.persist(flatEntity1);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		customerEntity1.addFlatEntity(flatEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("This flat has already an order assigned");
		orderService.createReservationOrder(flatEntity1.getId(), customerEntity1.getId());
	}

	@Test
	public void shouldCreatePurchaseOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		// when
		FlatTO flatTO = orderService.createPurchaseOrder(flatEntity1.getId(), customerEntity1.getId());
		// then
		assertTrue(flatTO.getCustomerIds().contains(customerEntity1.getId()));
		assertEquals(FlatStatus.SOLD, flatTO.getFlatStatus());

	}

	@Test()
	public void shouldThrowExceptionOnCreatePurchaseOrderWithFlatAlreadyAssignedToOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		em.persist(flatEntity1);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		customerEntity1.addFlatEntity(flatEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("This flat has already an order assigned");
		orderService.createPurchaseOrder(flatEntity1.getId(), customerEntity1.getId());
	}

	@Test
	public void shouldRemoveOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		CustomerEntity customerEntity2 = getCustomer();
		em.persist(customerEntity1);
		em.persist(customerEntity2);
		customerEntity1.addFlatEntity(flatEntity1);
		customerEntity2.addFlatEntity(flatEntity1);
		// when
		orderService.removeOrder(flatEntity1.getId());
		// then
		assertTrue(flatEntity1.getCustomerEntities().isEmpty());
		assertTrue(customerEntity1.getFlatEntities().isEmpty());
		assertTrue(customerEntity2.getFlatEntities().isEmpty());
		assertEquals(FlatStatus.FREE, flatEntity1.getFlatStatus());
	}

	@Test
	public void shouldThrowExceptionOnRemoveOrderWhenOrderWasNotFound() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such order doesn't exist");
		orderService.removeOrder(flatEntity1.getId());
	}

	@Test
	public void shouldThrowExceptionOnRemoveOrderWhenFlatWasNotFound() {
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such flat doesn't exist");
		orderService.removeOrder(1L);
	}

	@Test
	public void shouldAddCustomerToOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		CustomerEntity customerEntity2 = getCustomer();
		em.persist(customerEntity1);
		em.persist(customerEntity2);
		customerEntity1.addFlatEntity(flatEntity1);
		// when
		orderService.addCustomerToOrder(customerEntity2.getId(), flatEntity1.getId());
		// then
		assertTrue(flatEntity1.getCustomerEntities().contains(customerEntity1));
		assertEquals(FlatStatus.RESERVED, flatEntity1.getFlatStatus());
	}

	@Test
	public void shouldRemoveCustomerFromOrder() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		CustomerEntity customerEntity2 = getCustomer();
		em.persist(customerEntity1);
		em.persist(customerEntity2);
		customerEntity1.addFlatEntity(flatEntity1);
		customerEntity2.addFlatEntity(flatEntity1);
		// when
		orderService.removeCustomerFromOrder(customerEntity2.getId(), flatEntity1.getId());
		// then
		assertTrue(flatEntity1.getCustomerEntities().contains(customerEntity1));
		assertTrue(!flatEntity1.getCustomerEntities().contains(customerEntity2));
		assertEquals(FlatStatus.RESERVED, flatEntity1.getFlatStatus());
	}

	@Test
	public void shouldRemoveTheOnlyCustomerFromOrderAndSetStatusToFree() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		customerEntity1.addFlatEntity(flatEntity1);
		// when
		orderService.removeCustomerFromOrder(customerEntity1.getId(), flatEntity1.getId());
		// then
		assertTrue(!flatEntity1.getCustomerEntities().contains(customerEntity1));
		assertEquals(FlatStatus.FREE, flatEntity1.getFlatStatus());
	}

	@Test
	public void shouldThrowExceptionOnRemoveCustomerFromOrderWhenOrderWasNotFound() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such order doesn't exist");
		orderService.removeCustomerFromOrder(customerEntity1.getId(), flatEntity1.getId());
	}

	@Test
	public void shouldThrowExceptionOnRemoveCustomerFromOrderWhenFlatWasNotFound() {
		// given
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such flat doesn't exist");
		orderService.removeCustomerFromOrder(customerEntity1.getId(), 1L);
	}

	@Test
	public void shouldThrowExceptionOnRemoveCustomerFromOrderWhenCustomerWasNotFound() {
		// given
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.addCustomerEntity(customerEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such customer doesn't exist");
		orderService.removeCustomerFromOrder(10L, flatEntity1.getId());
	}

	@Test
	public void shouldSetOrderStatusToPurchase() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		customerEntity1.addFlatEntity(flatEntity1);
		// when
		orderService.setOrderStatusToPurchase(flatEntity1.getId());
		// then
		assertEquals(FlatStatus.SOLD, flatEntity1.getFlatStatus());
	}
	
	@Test
	public void shouldThrowExceptionOnSetOrderStatusToPurchaseWhenFlatWasNotFound() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		customerEntity1.addFlatEntity(flatEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such flat doesn't exist");
		orderService.setOrderStatusToPurchase(10L);
	}
	
	@Test
	public void shouldThrowExceptionOnSetOrderStatusToPurchaseWhenOrderWasNotFound() {
		// given
		FlatEntity flatEntity1 = getFlat();
		em.persist(flatEntity1);
		flatEntity1.setFlatStatus(FlatStatus.RESERVED);
		CustomerEntity customerEntity1 = getCustomer();
		em.persist(customerEntity1);
		// when
		expectedEx.expect(InvalidOrderPlacedException.class);
		expectedEx.expectMessage("Such order doesn't exist");
		orderService.setOrderStatusToPurchase(flatEntity1.getId());
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
