package com.capgemini.types;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.capgemini.domain.FlatStatus;

public class FlatTO extends AbstractTO {

	private String flatNumber;
	private String floorNumber;
	private int floorArea;
	private int numberOfRooms;
	private int numberOfBalconies;
	private int price;
	private FlatStatus flatStatus;
	private BuildingTO buildingTO;
	private Set<Long> customerIds = new HashSet<>();

	public FlatTO(Long id, Date created, Date updated, String flatNumber, int floorArea, int numberOfRooms,
			int numberOfBalconies, int price, FlatStatus flatStatus, BuildingTO buildingTO,
			Set<Long> customerIds, String floorNumber) {
		super(id, created, updated);
		this.flatNumber = flatNumber;
		this.floorArea = floorArea;
		this.numberOfRooms = numberOfRooms;
		this.numberOfBalconies = numberOfBalconies;
		this.price = price;
		this.flatStatus = flatStatus;
		this.buildingTO = buildingTO;
		this.customerIds = customerIds;
		this.floorNumber = floorNumber;
	}

	public String getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	public int getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(int floorArea) {
		this.floorArea = floorArea;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getNumberOfBalconies() {
		return numberOfBalconies;
	}

	public void setNumberOfBalconies(int numberOfBalconies) {
		this.numberOfBalconies = numberOfBalconies;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public FlatStatus getFlatStatus() {
		return flatStatus;
	}

	public void setFlatStatus(FlatStatus flatStatus) {
		this.flatStatus = flatStatus;
	}

	public BuildingTO getBuildingTO() {
		return buildingTO;
	}

	public void setBuildingTO(BuildingTO buildingTO) {
		this.buildingTO = buildingTO;
	}


	@Override
	public int hashCode() {
		return Objects.hash(getId(), getCreatedOn(), getUpdatedOn(), flatNumber, floorArea, floorNumber,numberOfRooms,
				numberOfBalconies, price, flatStatus, customerIds,buildingTO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlatTO flatTO = (FlatTO) obj;
		return Objects.equals(getId(), flatTO.getId()) 
				&& Objects.equals(getCreatedOn(), flatTO.getCreatedOn())
				&& Objects.equals(getUpdatedOn(), flatTO.getUpdatedOn()) 
				&& Objects.equals(flatNumber, flatTO.flatNumber)
				&& Objects.equals(buildingTO, flatTO.buildingTO)
				&& Objects.equals(customerIds, flatTO.customerIds)
				&& floorArea == flatTO.floorArea
				&& numberOfRooms == flatTO.numberOfRooms
				&& numberOfBalconies == flatTO.numberOfBalconies 
				&& price == flatTO.price
				&& flatStatus == flatTO.flatStatus
				&& floorNumber == flatTO.floorNumber;
				
	}

	public Set<Long> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(Set<Long> customerIds) {
		this.customerIds = customerIds;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public static class FlatTOBuilder extends AbstractTOBuilder<FlatTOBuilder> {
	
		private String flatNumber;
		private String floorNumber;
		private int floorArea;
		private int numberOfRooms;
		private int numberOfBalconies;
		private int price;
		private FlatStatus flatStatus;
		private BuildingTO buildingTO;
		private Set<Long> customerIds = new HashSet<>();

		public FlatTOBuilder() {
			super();
			super.setExtendingTOBuilder(this);
		}

		public FlatTOBuilder withFlatNumber(String flatNumber) {
			this.flatNumber = flatNumber;
			return this;
		}
		
		public FlatTOBuilder withFloorNumber(String floorNumber) {
			this.floorNumber = floorNumber;
			return this;
		}

		public FlatTOBuilder withFloorArea(int floorArea) {
			this.floorArea = floorArea;
			return this;
		}

		public FlatTOBuilder withNumberOfRooms(int numberOfRooms) {
			this.numberOfRooms = numberOfRooms;
			return this;
		}

		public FlatTOBuilder withNumberOfBalconies(int numberOfBalconies) {
			this.numberOfBalconies = numberOfBalconies;
			return this;
		}

		public FlatTOBuilder withPrice(int price) {
			this.price = price;
			return this;
		}

		public FlatTOBuilder withFlatStatus(FlatStatus flatStatus) {
			this.flatStatus = flatStatus;
			return this;
		}

		public FlatTOBuilder withBuildingTO(BuildingTO buildingTO) {
			this.buildingTO = buildingTO;
			return this;
		}

		public FlatTOBuilder withCustomerIds(Set<Long> customerIds) {
			this.customerIds = customerIds;
			return this;
		}
		
		public FlatTO build() {
			checkBeforeBuild(flatNumber, floorArea, numberOfRooms, price, flatStatus, buildingTO,floorNumber);
			return new FlatTO(id,createdOn, updatedOn, flatNumber, floorArea,
					numberOfRooms, numberOfBalconies, price, flatStatus, buildingTO, customerIds,floorNumber);
		}

		private void checkBeforeBuild(String flatNumber, int floorArea, int numberOfRooms, int price,
				FlatStatus flatStatus, BuildingTO buildingTO,String floorNumber) {
			if (flatNumber == null || flatNumber.isEmpty() || floorArea == 0 || numberOfRooms == 0 || price == 0
					|| flatStatus == null || buildingTO == null||floorNumber==null) {
				throw new RuntimeException("Incorrect flat transfer object to be created");
			}

		}
	}
}
