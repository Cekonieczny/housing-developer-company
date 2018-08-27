package com.capgemini.mappers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.types.BuildingTO;
import com.capgemini.types.FlatTO;
import com.capgemini.types.FlatTO.FlatTOBuilder;


@Service
public class FlatMapper {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private CustomerMapper cm;
	@Autowired
	private BuildingMapper bm;
	
	public  FlatTO toFlatTO(FlatEntity flatEntity) {
		if (flatEntity == null)
			return null;

		BuildingTO buildingTO = bm.toBuildingTO(flatEntity.getBuildingEntity());
		Set<Long> customerIds = cm.map2Ids(flatEntity.getCustomerEntities());

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
				.withUpdatedOn(flatEntity.getUpdatedOn())
				.withFloorNumber(flatEntity.getFloorNumber()).build();
	}

	public  FlatEntity toFlatEntity(FlatTO flatTO) {
		if (flatTO == null)
			return null;

		BuildingEntity buildingEntity = bm.toBuildingEntity(flatTO.getBuildingTO());
		Set<CustomerEntity> customerEntities = cm.map2Entities(flatTO.getCustomerIds());

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
		flatEntity.setFloorNumber(flatTO.getFloorNumber());

		return flatEntity;
	}

	public  Set<Long> map2Ids(Set<FlatEntity> flatEntities) {
		return flatEntities.stream().map(FlatEntity::getId).collect(Collectors.toSet());
	}
	
	public   Set<FlatTO> map2TOs(Set<FlatEntity> flatEntities) {
		return flatEntities.stream().map(this::toFlatTO).collect(Collectors.toSet());
	}

	public  Set<FlatEntity> map2Entities(Set<Long> flatIds) {
		if(flatIds.isEmpty()){
			return new HashSet<>();
		}
		List<Long> listOfIds = flatIds.stream().collect(Collectors.toList());
		List<FlatEntity> flatEntities = em.createQuery("SELECT f FROM FlatEntity f WHERE f.id IN :flatIds").setParameter("flatIds", listOfIds).getResultList();
		return flatEntities.stream().collect(Collectors.toSet());
	}
}
