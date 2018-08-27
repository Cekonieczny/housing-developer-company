package com.capgemini.dao.custom;

import java.util.List;

import com.capgemini.domain.BuildingEntity;

public interface BuildingDaoCustom {
	Double findAvgFlatPriceInACertainBuilding(Long buildingId);

	List<BuildingEntity> findBuildingWithMostQuantityOfFlatsFree();
}
