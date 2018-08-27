package com.capgemini.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.capgemini.domain.FlatStatus;
import com.capgemini.embeddable.Location;
import com.capgemini.types.BuildingTO;
import com.capgemini.types.BuildingTO.BuildingTOBuilder;
import com.capgemini.types.FlatTO;
import com.capgemini.types.FlatTO.FlatTOBuilder;

public class TOtest {

	@Test
	public void testBuildingBuilder() {
		// given
		Location location = new Location();
		location.setBuildingNumber("buildingnumber");
		location.setPlaceName("placename");
		location.setStreetName("streetname");
		location.setZipCode("zipcode");
		Date date = new Date();

		// when
		Long id = 1L;
		BuildingTO buildingTO = new BuildingTOBuilder().withLocation(location).withCreatedOn(date)
				.withDescription("sasadasdd").withHasLift(true).withId(id).withNumberOfFlats(11).withNumberOfFloors(13)
				.withUpdatedOn(date).build();
		

		// then
		assertEquals(location, buildingTO.getLocation());
		assertEquals(date, buildingTO.getCreatedOn());
		assertEquals(date, buildingTO.getUpdatedOn());
		assertEquals("sasadasdd", buildingTO.getDescription());
		assertEquals(true, buildingTO.hasLift());
		assertEquals(id, buildingTO.getId());
		Assert.assertEquals(11, buildingTO.getNumberOfFlats());
		Assert.assertEquals(13, buildingTO.getNumberOfFloors());
	}

	@Test
	public void testFlatBuilder() {
		// given
		String flatNumber = "11";
		int floorArea = 40;
		int numberOfBalconies = 2;
		int numberOfRooms = 3;
		int price = 200000000;
		Long flatId = 25l;
		Set<Long> customerIds = new HashSet<>();
		Location location = new Location();
		location.setBuildingNumber("buildingnumber");
		location.setPlaceName("placename");
		location.setStreetName("streetname");
		location.setZipCode("zipcode");
		Date date = new Date();
		Long buildingId = 200L;
		BuildingTO buildingTO = new BuildingTOBuilder().withLocation(location).withCreatedOn(date)
				.withDescription("sasadasdd").withHasLift(true).withId(buildingId).withNumberOfFlats(11).withNumberOfFloors(13)
				.withUpdatedOn(date).build();

		// when
		FlatTO flatTO = new FlatTOBuilder().withBuildingTO(buildingTO).withCreatedOn(date)
				.withCustomerIds(customerIds).withFlatNumber(flatNumber).withFlatStatus(FlatStatus.FREE).withFloorArea(floorArea)
				.withId(flatId).withNumberOfBalconies(numberOfBalconies).withNumberOfRooms(numberOfRooms).withPrice(price).withUpdatedOn(date)
				.build();

		// then
		assertEquals(flatNumber, flatTO.getFlatNumber());
		assertEquals(date, flatTO.getCreatedOn());
		assertEquals(date, flatTO.getUpdatedOn());
		assertEquals(flatId, flatTO.getId());
		assertEquals(customerIds, flatTO.getCustomerIds());
		Assert.assertEquals(floorArea, flatTO.getFloorArea());
		Assert.assertEquals(numberOfBalconies, flatTO.getNumberOfBalconies());
		Assert.assertEquals(numberOfRooms, flatTO.getNumberOfRooms());
		Assert.assertEquals(price, flatTO.getPrice());
	}
}
