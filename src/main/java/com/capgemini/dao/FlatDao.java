package com.capgemini.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.dao.custom.FlatDaoCustom;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;

@Repository
public interface FlatDao extends CrudRepository<FlatEntity, Long>, FlatDaoCustom {
	@Query("SELECT f FROM FlatEntity f JOIN f.customerEntities c WHERE c.id = ?1 AND f.flatStatus = ?2")
	List<FlatEntity> findFlatsByCustomerIdAndStatus(Long customerId, FlatStatus flatStatus);
}
