package com.capgemini.embeddable;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 10)
	String zipCode;
	@Column(nullable = false)
	String streetName;
	@Column(nullable = false, length = 10)
	String buildingNumber;
	@Column(nullable = false)
	String placeName;

	// for hibernate
	public Location() {

	}

	public Location(String zipCode, String streetName, String streetNumber, String placeName) {
		this.zipCode = zipCode;
		this.streetName = streetName;
		this.buildingNumber = streetNumber;
		this.placeName = placeName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(zipCode, streetName, buildingNumber, placeName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location location = (Location) obj;
		return Objects.equals(zipCode, location.zipCode) 
				&& Objects.equals(streetName, location.streetName)
				&& Objects.equals(buildingNumber, location.buildingNumber)
				&& Objects.equals(placeName, location.placeName);
	}
}
