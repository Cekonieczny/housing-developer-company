package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.BuildingDao;
import com.capgemini.dao.FlatDao;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.mappers.FlatMapper;
import com.capgemini.types.FlatTO;

public class FlatServiceImpl {

	@Autowired
	FlatDao flatRepository;

	@Autowired
	BuildingDao buildingRepository;

	public FlatTO saveOrUpdate(FlatTO flatTO) {
		FlatEntity flatEntity = flatRepository.save(FlatMapper.toFlatEntity(flatTO));
		return FlatMapper.toFlatTO(flatEntity);
	}

	public FlatTO findOne(Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		return FlatMapper.toFlatTO(flatEntity);
	}

	public void delete(Long flatId) {
		flatRepository.delete(flatId);
	}

	@Transactional
	public FlatTO addFlatToBuilding(Long flatId, Long buildingId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		BuildingEntity buildingEntity = buildingRepository.findOne(buildingId);

		buildingEntity.addFlatEntity(flatEntity);

		return FlatMapper.toFlatTO(flatEntity);
	}
	
	@Transactional
	public FlatTO removeFlatFromBuilding(Long flatId, Long buildingId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		BuildingEntity buildingEntity = buildingRepository.findOne(buildingId);

		buildingEntity.removeFlatEntity(flatEntity);

		return FlatMapper.toFlatTO(flatEntity);
	}

}
