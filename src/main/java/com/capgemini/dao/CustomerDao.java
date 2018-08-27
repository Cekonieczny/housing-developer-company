package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.dao.custom.CustomerDaoCustom;
import com.capgemini.domain.CustomerEntity;

@Repository
public interface CustomerDao extends CrudRepository<CustomerEntity,Long>, CustomerDaoCustom {

}
