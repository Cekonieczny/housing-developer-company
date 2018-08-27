package com.capgemini.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	CustomerDao customerRepository;

	@Autowired
	FlatDao flatRepository;
	
	@Autowired
	CustomerMapper cm;
	
	@Autowired
	FlatMapper fm;

	
	@Override
	public Set<CustomerTO> findCustomersByPlacedOrder(Long flatId) {
		return cm.map2TOs(flatRepository.findOne(flatId).getCustomerEntities());
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
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.setFlatStatus(FlatStatus.RESERVED);
		flatEntity.addCustomerEntity(customerEntity);
		return fm.toFlatTO(flatEntity);
	}


	@Override
	@Transactional
	public FlatTO createPurchaseOrder(Long flatId, Long customerId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		if (!flatEntity.getCustomerEntities().isEmpty()) {
			throw new InvalidOrderPlacedException("This flat has already an order assigned");
		}
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.setFlatStatus(FlatStatus.SOLD);
		flatEntity.addCustomerEntity(customerEntity);
		return fm.toFlatTO(flatEntity);
	}
	

	@Override
	@Transactional
	public void removeOrder(Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		Set<CustomerEntity> customerEntities = flatEntity.getCustomerEntities();
		for (CustomerEntity c : customerEntities) {
				c.setFlatEntities(new HashSet<>());
			}
		flatEntity.setFlatStatus(FlatStatus.FREE);
		flatEntity.setCustomerEntities(new HashSet<>());
	}

	@Override
	@Transactional
	public CustomerTO addCustomerToOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.addCustomerEntity(customerEntity);
		return cm.toCustomerTO(customerEntity);
	}


	@Override
	@Transactional
	public CustomerTO removeCustomerFromOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		if(customerEntity==null){
			throw new InvalidOrderPlacedException("Such customer doesn't exist");
		}
		flatEntity.removeCustomerEntity(customerEntity);

		if (flatEntity.getCustomerEntities().isEmpty()) {
			flatEntity.setFlatStatus(FlatStatus.FREE);
		}
		return cm.toCustomerTO(customerEntity);
	}


	@Override
	@Transactional
	public void setOrderStatusToPurchase(Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		doesOrderExist(flatEntity);
		flatEntity.setFlatStatus(FlatStatus.SOLD);
	}

	private boolean isCustomerReservingMoreThanThreeFlatsByHimself(Long customerId) {
		Set<FlatEntity> reservedFlats = flatRepository.findFlatsByCustomerIdAndStatus(customerId,FlatStatus.RESERVED).stream().collect(Collectors.toSet());;
		int count = 0;
		for (FlatEntity f : reservedFlats) {
			if (f.getCustomerEntities().size() == 1) {
				count++;
			}
		}
		return count > 3;
	}
	
	private boolean doesOrderExist(FlatEntity flatEntity){
		if(flatEntity == null){
			throw new InvalidOrderPlacedException("Such flat doesn't exist");
		}
		if(flatEntity.getCustomerEntities().isEmpty()){
			throw new InvalidOrderPlacedException("Such order doesn't exist");
		}	
		else {
			return true;
		}
	}
}
