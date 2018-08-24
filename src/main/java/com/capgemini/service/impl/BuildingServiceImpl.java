package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.dao.BuildingDao;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.mappers.BuildingMapper;
import com.capgemini.types.BuildingTO;

public class BuildingServiceImpl {
	@Autowired
	BuildingDao buildingRepository;
	
	public BuildingTO saveOrUpdate(BuildingTO buildingTO){
		BuildingEntity buildingEntity = buildingRepository.save(BuildingMapper.toBuildingEntity(buildingTO));
		return BuildingMapper.toBuildingTO(buildingEntity);
	}
	
	public BuildingTO findOne(Long buildingId){
		BuildingEntity buildingEntity = buildingRepository.findOne(buildingId);
		return BuildingMapper.toBuildingTO(buildingEntity);
	}
	
	public void delete(Long buildingId){
		buildingRepository.delete(buildingId);
	}
}
