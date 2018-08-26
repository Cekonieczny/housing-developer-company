package com.capgemini.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.FlatDao;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.mappers.FlatMapper;
import com.capgemini.service.OrderService;
import com.capgemini.service.exceptions.InvalidOrderPlacedException;
import com.capgemini.types.CustomerTO;
import com.capgemini.types.FlatTO;

public class OrderServiceImpl implements OrderService {

	@Autowired
	CustomerDao customerRepository;

	@Autowired
	FlatDao flatRepository;

	
	@Override
	public Set<CustomerTO> findCustomersByPlacedOrder(Long flatId) {
		return CustomerMapper.map2TOs(flatRepository.findOne(flatId).getCustomerEntities());
	}

	
	@Override
	@Transactional
	public FlatTO createReservationOrder(Long flatId, Long customerId) {
		if (isCustomerReservingMoreThanThreeFlatsByHimself(customerId)) {
			throw new InvalidOrderPlacedException("This customer has already reserved 3 flats on his/her own");
		}
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		if (!flatEntity.getCustomerEntities().isEmpty()) {
			throw new InvalidOrderPlacedException("This flat has already an order assigned");
		}
		flatEntity.setFlatStatus(FlatStatus.RESERVED);
		return FlatMapper.toFlatTO(flatEntity);
	}


	@Override
	@Transactional
	public FlatTO createPurchaseOrder(Long flatId, Long customerId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		if (!flatEntity.getCustomerEntities().isEmpty()) {
			throw new InvalidOrderPlacedException("This flat has already an order assigned");
		}
		flatEntity.setFlatStatus(FlatStatus.SOLD);
		return FlatMapper.toFlatTO(flatEntity);
	}
	

	@Override
	@Transactional
	public void removeOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.setCustomerEntities(new HashSet<>());
		customerEntity.setFlatEntities(new HashSet<>());
	}

	@Override
	@Transactional
	public CustomerTO addCustomerToOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.addCustomerEntity(customerEntity);
		return CustomerMapper.toCustomerTO(customerEntity);
	}


	@Override
	@Transactional
	public CustomerTO removeCustomerFromOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.removeCustomerEntity(customerEntity);

		if (flatEntity.getCustomerEntities().isEmpty()) {
			flatEntity.setFlatStatus(FlatStatus.FREE);
		}
		return CustomerMapper.toCustomerTO(customerEntity);
	}


	@Override
	@Transactional
	public void setOrderStatusToPurchase(Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		flatEntity.setFlatStatus(FlatStatus.SOLD);
	}

	private boolean isCustomerReservingMoreThanThreeFlatsByHimself(Long customerId) {
		Set<FlatEntity> reservedFlats = flatRepository.findReservedFlatsByCustomerId(customerId);
		int count = 0;
		for (FlatEntity f : reservedFlats) {
			if (f.getCustomerEntities().size() == 1) {
				count++;
			}
		}
		return count > 3;
	}
	
	private boolean doesOrderExist(FlatEntity flatEntity){
		if(flatEntity == null || flatEntity.getFlatStatus() == FlatStatus.FREE){
			throw new InvalidOrderPlacedException("Such order doesn't exist");
		}
		else {
			return true;
		}
	}
}
