package com.capgemini.service;

import org.springframework.stereotype.Service;

import com.capgemini.types.CustomerTO;
@Service
public interface CustomerService {

	CustomerTO saveOrUpdate(CustomerTO customerTO);

	CustomerTO findOne(Long customerId);

	void delete(Long customerId);

}