package com.capgemini.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.types.BuildingTO;
import com.capgemini.types.BuildingTO.BuildingTOBuilder;
@Service
public class BuildingMapper {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private FlatMapper fm;

	public  BuildingTO toBuildingTO(BuildingEntity buildingEntity) {
		if (buildingEntity == null)
			return null;
		
		Set<Long> flatIds = fm.map2Ids(buildingEntity.getFlatEntities());
		
		return new BuildingTOBuilder()
				.withNumberOfFloors(buildingEntity.getNumberOfFloors())
				.withDescription(buildingEntity.getDescription())
				.withHasLift(buildingEntity.hasLift())
				.withLocation(buildingEntity.getLocation())
				.withNumberOfFlats(buildingEntity.getNumberOfFlats())
				.withNumberOfFloors(buildingEntity.getNumberOfFloors())
				.withUpdatedOn(buildingEntity.getUpdatedOn())
				.withId(buildingEntity.getId())
				.withCreatedOn(buildingEntity.getCreatedOn())
				.withFlatIds(flatIds).build();
	}

	public  BuildingEntity toBuildingEntity(BuildingTO buildingTO) {
		if (buildingTO == null)
			return null;
		
		Set<FlatEntity> flatEntities = fm.map2Entities(buildingTO.getFlatIds());

		BuildingEntity buildingEntity = new BuildingEntity();
		buildingEntity.setCreatedOn(buildingTO.getCreatedOn());
		buildingEntity.setDescription(buildingTO.getDescription());
		buildingEntity.setHasLift(buildingTO.hasLift());
		buildingEntity.setId(buildingTO.getId());
		buildingEntity.setLocation(buildingTO.getLocation());
		buildingEntity.setNumberOfFlats(buildingTO.getNumberOfFlats());
		buildingEntity.setNumberOfFloors(buildingTO.getNumberOfFloors());
		buildingEntity.setUpdatedOn(buildingTO.getUpdatedOn());
		buildingEntity.setFlatEntities(flatEntities);

		return buildingEntity;
	}

	public  Set<Long> map2Ids(Set<BuildingEntity> buildingEntities) {
		return buildingEntities.stream().map(BuildingEntity::getId).collect(Collectors.toSet());
	}
	
	public  Set<BuildingTO> map2TOs(Set<BuildingEntity> buildingEntities) {
		return buildingEntities.stream().map(this::toBuildingTO).collect(Collectors.toSet());
	}

	public  Set<BuildingEntity> map2Entities(Set<Long> buildingIds) {
		TypedQuery<BuildingEntity> q = em.createQuery("SELECT b FROM BuildingEntity WHERE b.id in :buildingIds",
				BuildingEntity.class);
		q.setParameter("buildingIds", buildingIds);
		return q.getResultList().stream().collect(Collectors.toSet());
	}
}
