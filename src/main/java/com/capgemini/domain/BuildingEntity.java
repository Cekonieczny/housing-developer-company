package com.capgemini.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.capgemini.embeddable.Location;

@Entity
@EntityListeners(TimestampListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "BUILDING")
public class BuildingEntity extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Location location;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private int numberOfFloors;

	@Column(nullable = false)
	private int numberOfFlats;

	@Column(nullable = false)
	private boolean hasLift;

	@OneToMany(mappedBy = "buildingEntity", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<FlatEntity> flatEntities = new HashSet<>();

	public BuildingEntity() {
		super();
	}

	public BuildingEntity(Long id, Location location, String description, int numberOfFloors, int numberOfFlats,
			boolean hasLift, Set<FlatEntity> flatEntities) {
		super(id);
		this.location = location;
		this.description = description;
		this.numberOfFloors = numberOfFloors;
		this.numberOfFlats = numberOfFlats;
		this.hasLift = hasLift;
		this.flatEntities = flatEntities;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	public int getNumberOfFlats() {
		return numberOfFlats;
	}

	public void setNumberOfFlats(int numberOfFlats) {
		this.numberOfFlats = numberOfFlats;
	}

	public boolean hasLift() {
		return hasLift;
	}

	public void setHasLift(boolean hasLift) {
		this.hasLift = hasLift;
	}

	public Set<FlatEntity> getFlatEntities() {
		return flatEntities;
	}

	public void setFlatEntities(Set<FlatEntity> flatEntities) {
		this.flatEntities = flatEntities;
	}
	
	public void addFlatEntity(FlatEntity flatEntity){
		this.flatEntities.add(flatEntity);
		flatEntity.setBuildingEntity(this);
	}
	
	public void removeFlatEntity(FlatEntity flatEntity){
		this.flatEntities.remove(flatEntity);
		flatEntity.setBuildingEntity(null);
	}
	

}
