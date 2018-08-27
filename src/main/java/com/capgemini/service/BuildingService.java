package com.capgemini.service;

import org.springframework.stereotype.Service;

import com.capgemini.types.BuildingTO;
@Service
public interface BuildingService {

	BuildingTO saveOrUpdate(BuildingTO buildingTO);

	BuildingTO findOne(Long buildingId);

	void delete(Long buildingId);

}