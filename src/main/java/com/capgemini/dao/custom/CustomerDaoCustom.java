package com.capgemini.dao.custom;

import java.util.Set;

import com.capgemini.domain.CustomerEntity;

public interface CustomerDaoCustom {
	Integer findSumOfFlatPricesBoughtByGivenCustomer(Long customerId);

	Set<CustomerEntity> findCustomersWhoBoughtMoreThanOneFlat();
}
