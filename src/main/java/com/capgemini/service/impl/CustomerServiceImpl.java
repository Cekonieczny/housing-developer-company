package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capgemini.dao.CustomerDao;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.service.CustomerService;
import com.capgemini.types.CustomerTO;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDao customerRepository;
	
	@Autowired
	CustomerMapper cm;


	@Override
	public CustomerTO saveOrUpdate(CustomerTO customerTO) {
		CustomerEntity customerEntity = customerRepository.save(cm.toCustomerEntity(customerTO));
		return cm.toCustomerTO(customerEntity);
	}


	@Override
	public CustomerTO findOne(Long customerId) {
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		return cm.toCustomerTO(customerEntity);
	}


	@Override
	public void delete(Long customerId) {
		customerRepository.delete(customerId);
	}
}
