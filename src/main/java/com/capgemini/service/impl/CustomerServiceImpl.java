package com.capgemini.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.capgemini.dao.CustomerDao;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.mappers.CustomerMapper;
import com.capgemini.types.CustomerTO;

@Service
public class CustomerServiceImpl {

	@Autowired
	CustomerDao customerRepository;

	public CustomerTO saveOrUpdate(CustomerTO customerTO) {
		CustomerEntity customerEntity = customerRepository.save(CustomerMapper.toCustomerEntity(customerTO));
		return CustomerMapper.toCustomerTO(customerEntity);
	}

	public CustomerTO findOne(Long customerId) {
		CustomerEntity customerEntity = customerRepository.findOne(customerId);
		return CustomerMapper.toCustomerTO(customerEntity);
	}

	public void delete(Long customerId) {
		customerRepository.delete(customerId);
	}
}
