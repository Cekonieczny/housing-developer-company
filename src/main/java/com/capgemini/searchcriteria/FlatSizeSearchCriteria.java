package com.capgemini.searchcriteria;

import java.util.Objects;

public class FlatSizeSearchCriteria {

	private Integer floorArea;

	private Integer numberOfRooms;

	private Integer numberOfBalconies;

	public FlatSizeSearchCriteria(Integer floorArea, Integer numberOfRooms, Integer numberOfBalconies) {
		this.floorArea = floorArea;
		this.numberOfRooms = numberOfRooms;
		this.numberOfBalconies = numberOfBalconies;
	}

	public FlatSizeSearchCriteria() {
	}

	public Integer getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(Integer floorArea) {
		this.floorArea = floorArea;
	}

	public Integer getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(Integer numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public Integer getNumberOfBalconies() {
		return numberOfBalconies;
	}

	public void setNumberOfBalconies(Integer numberOfBalconies) {
		this.numberOfBalconies = numberOfBalconies;
	}

	@Override
	public int hashCode() {
		return Objects.hash(floorArea, numberOfRooms, numberOfBalconies);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlatSizeSearchCriteria flatSizeSearchCriteria = (FlatSizeSearchCriteria) obj;
		return floorArea == flatSizeSearchCriteria.getFloorArea()
				&& numberOfRooms == flatSizeSearchCriteria.getFloorArea()
				&& numberOfBalconies == flatSizeSearchCriteria.getFloorArea();

	}

}
