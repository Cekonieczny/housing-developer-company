package com.capgemini.service;

import org.springframework.stereotype.Service;

import com.capgemini.types.FlatTO;
@Service
public interface FlatService {

	FlatTO saveOrUpdate(FlatTO flatTO);

	FlatTO findOne(Long flatId);

	void delete(Long flatId);

	FlatTO addFlatToBuilding(Long flatId, Long buildingId);

	FlatTO removeFlatFromBuilding(Long flatId, Long buildingId);

}