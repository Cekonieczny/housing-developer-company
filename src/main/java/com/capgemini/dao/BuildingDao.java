package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.dao.custom.BuildingDaoCustom;
import com.capgemini.domain.BuildingEntity;

@Repository
public interface BuildingDao extends CrudRepository<BuildingEntity,Long> , BuildingDaoCustom {

}
