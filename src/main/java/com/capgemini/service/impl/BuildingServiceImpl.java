package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.dao.BuildingDao;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.mappers.BuildingMapper;
import com.capgemini.service.BuildingService;
import com.capgemini.types.BuildingTO;
@Service
public class BuildingServiceImpl implements BuildingService {
	@Autowired
	BuildingDao buildingRepository;
	
	@Autowired
	BuildingMapper bm;
	
	@Override
	public BuildingTO saveOrUpdate(BuildingTO buildingTO){
		BuildingEntity buildingEntity = buildingRepository.save(bm.toBuildingEntity(buildingTO));
		return bm.toBuildingTO(buildingEntity);
	}
	
	@Override
	public BuildingTO findOne(Long buildingId){
		BuildingEntity buildingEntity = buildingRepository.findOne(buildingId);
		return bm.toBuildingTO(buildingEntity);
	}
	
	@Override
	public void delete(Long buildingId){
		buildingRepository.delete(buildingId);
	}
}
