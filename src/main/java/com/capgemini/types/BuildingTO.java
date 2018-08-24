package com.capgemini.types;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.capgemini.embeddable.Location;

public class BuildingTO extends AbstractTO {
	private Location location;
	private String description;
	private int numberOfFloors;
	private int numberOfFlats;
	private boolean hasLift;
	private Set<Long> flatIds = new HashSet<>();

	public BuildingTO(Long id, Date created, Date updated, Location location, String description, int numberOfFloors,
			int numberOfFlats, boolean hasLift,Set<Long> flatIds) {
		super(id, created, updated);
		this.location = location;
		this.description = description;
		this.numberOfFloors = numberOfFloors;
		this.numberOfFlats = numberOfFlats;
		this.hasLift = hasLift;
		this.flatIds = flatIds;
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

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getCreatedOn(), getUpdatedOn(), location, description, numberOfFloors,
				numberOfFlats, hasLift);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingTO buildingTO = (BuildingTO) obj;
		return Objects.equals(getId(), buildingTO.getId()) 
				&& Objects.equals(getCreatedOn(), buildingTO.getCreatedOn())
				&& Objects.equals(getUpdatedOn(), buildingTO.getUpdatedOn()) 
				&& Objects.equals(location, buildingTO.location)
				&& Objects.equals(description, buildingTO.description)
				&& Objects.equals(numberOfFloors, buildingTO.numberOfFloors)
				&& Objects.equals(numberOfFlats, buildingTO.numberOfFlats)
				&& Objects.equals(hasLift, buildingTO.hasLift) 
				&& Objects.equals(flatIds, buildingTO.flatIds);
	}

	public Set<Long> getFlatIds() {
		return flatIds;
	}

	public void setFlatIds(Set<Long> flatIds) {
		this.flatIds = flatIds;
	}

	public static class BuildingTOBuilder extends AbstractTOBuilder<BuildingTOBuilder> {
		private Location location;
		private String description;
		private int numberOfFloors;
		private int numberOfFlats;
		private boolean hasLift;
		private Set<Long> flatIds = new HashSet<>();

		public BuildingTOBuilder() {
			super();
			super.setExtendingTOBuilder(this);
		}

		public BuildingTOBuilder withLocation(Location location) {
			this.location = location;
			return this;
		}

		public BuildingTOBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public BuildingTOBuilder withNumberOfFloors(int numberOfFloors) {
			this.numberOfFloors = numberOfFloors;
			return this;
		}

		public BuildingTOBuilder withNumberOfFlats(int numberOfFlats) {
			this.numberOfFlats = numberOfFlats;
			return this;
		}

		public BuildingTOBuilder withHasLift(boolean hasLift) {
			this.hasLift = hasLift;
			return this;
		}

		public BuildingTOBuilder withFlatIds(Set<Long> flatIds) {
			this.flatIds = flatIds;
			return this;
		}

		public BuildingTO build() {
			checkBeforeBuild(location, description, numberOfFloors, numberOfFlats);
			return new BuildingTO(id, createdOn, updatedOn, location, description, numberOfFloors, numberOfFlats,
					hasLift,flatIds);
		}

		private void checkBeforeBuild(Location location, String description, int numberOfFloors, int numberOfFlats) {
			if (location == null || description == null || description.isEmpty() || numberOfFloors == 0
					|| numberOfFlats == 0) {
				throw new RuntimeException("Incorrect Building transfer object to be created");
			}
		}

	}
}
