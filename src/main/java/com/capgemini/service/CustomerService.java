package com.capgemini.service;

import java.util.Set;

import com.capgemini.types.CustomerTO;

public interface CustomerService {

	CustomerTO saveOrUpdate(CustomerTO customerTO);

	CustomerTO findOne(Long customerId);

	void delete(Long customerId);

	Set<CustomerTO> findCustomersByPlacedOrder(Long flatId);

	CustomerTO addCustomerToPurchaseOrder(Long customerId, Long flatId);

}