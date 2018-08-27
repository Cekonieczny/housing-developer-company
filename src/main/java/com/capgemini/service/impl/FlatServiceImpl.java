package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.BuildingDao;
import com.capgemini.dao.FlatDao;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.mappers.FlatMapper;
import com.capgemini.service.FlatService;
import com.capgemini.types.FlatTO;

@Service
public class FlatServiceImpl implements FlatService {

	@Autowired
	FlatDao flatRepository;

	@Autowired
	BuildingDao buildingRepository;
	
	@Autowired
	FlatMapper fm;

	
	@Override
	public FlatTO saveOrUpdate(FlatTO flatTO) {
		FlatEntity flatEntity = flatRepository.save(fm.toFlatEntity(flatTO));
		return fm.toFlatTO(flatEntity);
	}

	
	@Override
	public FlatTO findOne(Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		return fm.toFlatTO(flatEntity);
	}

	
	@Override
	public void delete(Long flatId) {
		flatRepository.delete(flatId);
	}

	
	@Override
	@Transactional
	public FlatTO addFlatToBuilding(Long flatId, Long buildingId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		BuildingEntity buildingEntity = buildingRepository.findOne(buildingId);

		buildingEntity.addFlatEntity(flatEntity);

		return fm.toFlatTO(flatEntity);
	}
	
	
	@Override
	@Transactional
	public FlatTO removeFlatFromBuilding(Long flatId, Long buildingId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		BuildingEntity buildingEntity = buildingRepository.findOne(buildingId);

		buildingEntity.removeFlatEntity(flatEntity);

		return fm.toFlatTO(flatEntity);
	}

}
