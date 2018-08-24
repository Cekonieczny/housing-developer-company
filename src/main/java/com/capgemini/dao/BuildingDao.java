package com.capgemini.dao;

import org.springframework.data.repository.CrudRepository;

import com.capgemini.dao.custom.BuildingDaoCustom;
import com.capgemini.domain.BuildingEntity;

public interface BuildingDao extends CrudRepository<BuildingEntity,Long> , BuildingDaoCustom {

}
