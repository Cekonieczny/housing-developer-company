package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;

import com.capgemini.domain.CustomerEntity;

public interface CustomerDao extends CrudRepository<CustomerEntity,Long> {

}
