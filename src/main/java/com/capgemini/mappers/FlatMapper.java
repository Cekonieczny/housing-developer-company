package com.capgemini.mappers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.types.BuildingTO;
import com.capgemini.types.FlatTO;
import com.capgemini.types.FlatTO.FlatTOBuilder;



public class FlatMapper {
	@PersistenceContext
	private static EntityManager em;
	
	public static FlatTO toFlatTO(FlatEntity flatEntity) {
		if (flatEntity == null)
			return null;

		BuildingTO buildingTO = BuildingMapper.toBuildingTO(flatEntity.getBuildingEntity());
		Set<Long> customerIds = CustomerMapper.map2Ids(flatEntity.getCustomerEntities());

		return new FlatTOBuilder().withBuildingTO(buildingTO)
				.withCreatedOn(flatEntity.getCreatedOn())
				.withCustomerIds(customerIds)
				.withFlatNumber(flatEntity.getFlatNumber())
				.withFlatStatus(flatEntity.getFlatStatus())
				.withFloorArea(flatEntity.getFloorArea())
				.withId(flatEntity.getId())
				.withNumberOfBalconies(flatEntity.getNumberOfBalconies())
				.withNumberOfRooms(flatEntity.getNumberOfRooms())
				.withPrice(flatEntity.getPrice())
				.withUpdatedOn(flatEntity.getUpdatedOn()).build();
	}

	public static FlatEntity toFlatEntity(FlatTO flatTO) {
		if (flatTO == null)
			return null;

		BuildingEntity buildingEntity = BuildingMapper.toBuildingEntity(flatTO.getBuildingTO());
		Set<CustomerEntity> customerEntities = CustomerMapper.map2Entities(flatTO.getCustomerIds());

		FlatEntity flatEntity = new FlatEntity();
		flatEntity.setBuildingEntity(buildingEntity);
		flatEntity.setCreatedOn(flatTO.getCreatedOn());
		flatEntity.setCustomerEntities(customerEntities);
		flatEntity.setFlatNumber(flatTO.getFlatNumber());
		flatEntity.setFlatStatus(flatTO.getFlatStatus());
		flatEntity.setFloorArea(flatTO.getFloorArea());
		flatEntity.setId(flatTO.getId());
		flatEntity.setNumberOfBalconies(flatTO.getNumberOfBalconies());
		flatEntity.setNumberOfRooms(flatTO.getNumberOfRooms());
		flatEntity.setPrice(flatTO.getPrice());
		flatEntity.setUpdatedOn(flatTO.getUpdatedOn());

		return flatEntity;
	}

	public static Set<Long> map2Ids(Set<FlatEntity> flatEntities) {
		return flatEntities.stream().map(FlatEntity::getId).collect(Collectors.toSet());
	}
	
	public static Set<FlatTO> map2TOs(Set<FlatEntity> flatEntities) {
		return flatEntities.stream().map(FlatMapper::toFlatTO).collect(Collectors.toSet());
	}

	public static Set<FlatEntity> map2Entities(Set<Long> flatIds) {
		if(flatIds.size()==0){
			return new HashSet<>();
		}
		TypedQuery<FlatEntity> q = em.createQuery("SELECT f FROM FlatEntity WHERE f.id in :flatIds", FlatEntity.class);
		q.setParameter("flatIds", flatIds);
			return q.getResultList().stream().collect(Collectors.toSet());
	}
}
