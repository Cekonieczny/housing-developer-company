package com.capgemini.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@EntityListeners(TimestampListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "FLAT")
public class FlatEntity extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 10)
	private String flatNumber;
	
	@Column(nullable = false, length = 6)
	private String floorNumber;

	@Column(nullable = false)
	private int floorArea;

	@Column(nullable = false)
	private int numberOfRooms;

	@Column(nullable = false)
	private int numberOfBalconies;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FlatStatus flatStatus = FlatStatus.FREE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BUILDING_ID", nullable = false)
	private BuildingEntity buildingEntity;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "FLAT_ORDER", joinColumns = { @JoinColumn(name = "customer_id") }, inverseJoinColumns = {
			@JoinColumn(name = "flat_id") })
	private Set<CustomerEntity> customerEntities = new HashSet<>();

	// for hibernate
	public FlatEntity() {
		super();
	}

	public FlatEntity(Long id, String flatNumber, String floorNumber, int floorArea, int numberOfRooms, int numberOfBalconies, int price,
			FlatStatus flatStatus, BuildingEntity buildingEntity, Set<CustomerEntity> customerEntities) {
		super(id);
		this.flatNumber = flatNumber;
		this.floorArea = floorArea;
		this.numberOfRooms = numberOfRooms;
		this.numberOfBalconies = numberOfBalconies;
		this.price = price;
		this.flatStatus = flatStatus;
		this.buildingEntity = buildingEntity;
		this.customerEntities = customerEntities;
		this.floorNumber=floorNumber;
	}

	
	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
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

	public BuildingEntity getBuildingEntity() {
		return buildingEntity;
	}

	public void setBuildingEntity(BuildingEntity buildingEntity) {
		this.buildingEntity = buildingEntity;
	}

	public Set<CustomerEntity> getCustomerEntities() {
		return customerEntities;
	}

	public void setCustomerEntities(Set<CustomerEntity> customerEntities) {
		this.customerEntities = customerEntities;
	}

	public void addCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntities.add(customerEntity);
		customerEntity.getFlatEntities().add(this);
	}

	public void removeCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntities.remove(customerEntity);
		customerEntity.getFlatEntities().remove(this);
	}
	
}
