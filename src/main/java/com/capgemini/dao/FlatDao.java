package com.capgemini.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.capgemini.dao.custom.FlatDaoCustom;
import com.capgemini.domain.FlatEntity;

public interface FlatDao extends CrudRepository<FlatEntity,Long>, FlatDaoCustom {
	@Query("SELECT f FROM FlatEntity f JOIN f.customerEntities c WHERE f.flatStatus = RESERVED AND c.id = ?1")
	Set<FlatEntity> findReservedFlatsByCustomerId(Long customerId);
}
