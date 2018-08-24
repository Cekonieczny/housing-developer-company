package com.capgemini.embeddable;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	@Embedded
	private Location location;

	@Column(length = 10)
	private String apartmentNumber;

	public Address() {

	}

	public Address(Location location, String apartmentNumber) {
		this.location = location;
		this.apartmentNumber = apartmentNumber;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(location, apartmentNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address address = (Address) obj;
		return Objects.equals(location, address.location) 
				&& Objects.equals(apartmentNumber, address.apartmentNumber);
	}

}
