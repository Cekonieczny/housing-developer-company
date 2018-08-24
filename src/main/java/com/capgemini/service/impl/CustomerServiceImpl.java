package com.capgemini.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.dao.CustomerDao;
import com.capgemini.dao.FlatDao;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.service.CustomerService;
import com.capgemini.service.exceptions.InvalidOrderPlacedException;
import com.capgemini.types.CustomerTO;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDao customerRepository;
	
	@Autowired
	FlatDao flatRepository;

	
	@Override
	public CustomerTO saveOrUpdate(CustomerTO customerTO) {
		CustomerEntity customerEntity = customerRepository.save(CustomerMapper.toCustomerEntity(customerTO));
		return CustomerMapper.toCustomerTO(customerEntity);
	}

	
	@Override
	public CustomerTO findOne(Long customerId) {
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		return CustomerMapper.toCustomerTO(customerEntity);
	}

	
	@Override
	public void delete(Long customerId) {
		customerRepository.delete(customerId);
	}
	
	
	@Override
	public Set<CustomerTO> findCustomersByPlacedOrder(Long flatId) {
		return CustomerMapper.map2TOs(flatRepository.findOne(flatId).getCustomerEntities());
	}

	@Transactional
	public CustomerTO addCustomerToReservationOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		if (flatEntity.getFlatStatus() == FlatStatus.SOLD) {
			throw new InvalidOrderPlacedException("this flat is already sold");
		}
		if (isCustomerReservingMoreThanThreeFlatsByHimself(customerId)) {
			throw new InvalidOrderPlacedException("this customer has already reserved 3 flats on his/her own");
		}
		if (flatEntity.getFlatStatus() == FlatStatus.FREE) {
			flatEntity.setFlatStatus(FlatStatus.RESERVED);
		}
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.addCustomerEntity(customerEntity);
		return CustomerMapper.toCustomerTO(customerEntity);
	}
	
	@Override
	@Transactional
	public CustomerTO addCustomerToPurchaseOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		if (flatEntity.getFlatStatus() == FlatStatus.RESERVED) {
			flatEntity.setFlatStatus(FlatStatus.SOLD);
		}
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		flatEntity.addCustomerEntity(customerEntity);
		return CustomerMapper.toCustomerTO(customerEntity);
	}
	
	@Transactional
	public CustomerTO removeCustomerFromOrder(Long customerId, Long flatId) {
		FlatEntity flatEntity = flatRepository.findOne(flatId);
		if (flatEntity.getFlatStatus() == FlatStatus.SOLD) {
			throw new InvalidOrderPlacedException("order status is sold, not reserved");
		}
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		
		flatEntity.removeCustomerEntity(customerEntity);

		if (flatEntity.getCustomerEntities().isEmpty()) {
			flatEntity.setFlatStatus(FlatStatus.FREE);
		}
	
		return CustomerMapper.toCustomerTO(customerEntity);
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
	
}
