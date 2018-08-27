package com.capgemini.dao.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.capgemini.dao.custom.CustomerDaoCustom;
import com.capgemini.domain.CustomerEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.domain.QCustomerEntity;
import com.capgemini.domain.QFlatEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomerDaoImpl implements CustomerDaoCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public Integer findSumOfFlatPricesBoughtByGivenCustomer(Long customerId) {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QCustomerEntity qcustomer = QCustomerEntity.customerEntity;
		QFlatEntity qflat = QFlatEntity.flatEntity;
		return qf.select(qflat.price.sum()).from(qcustomer)
				.join(qcustomer.flatEntities, qflat)
				.on(qflat.flatStatus.eq(FlatStatus.SOLD))
				.where(qcustomer.id.eq(customerId)).fetchOne();
	}

	@Override
	public Set<CustomerEntity> findCustomersWhoBoughtMoreThanOneFlat() {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QCustomerEntity qcustomer = QCustomerEntity.customerEntity;
		
		List<CustomerEntity> customerList = qf.selectFrom(qcustomer)
				.join(qcustomer.flatEntities, qflat)
				.on(qflat.flatStatus.eq(FlatStatus.SOLD))
				.groupBy(qcustomer).having(qflat.count().gt(1L)).fetch();

		return customerList.stream().collect(Collectors.toSet());
	}

}
