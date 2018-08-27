package com.capgemini.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.capgemini.types.CustomerTO;
import com.capgemini.types.FlatTO;

@Service
public interface OrderService {

	Set<CustomerTO> findCustomersByPlacedOrder(Long flatId);

	FlatTO createReservationOrder(Long flatId, Long customerId);

	FlatTO createPurchaseOrder(Long flatId, Long customerId);

	void removeOrder(Long flatId);

	CustomerTO addCustomerToOrder(Long customerId, Long flatId);

	CustomerTO removeCustomerFromOrder(Long customerId, Long flatId);

	void setOrderStatusToPurchase(Long flatId);

}