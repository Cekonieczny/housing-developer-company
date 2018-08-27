package com.capgemini.dao.custom;

import java.util.List;

import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.searchcriteria.FlatSizeSearchCriteria;

public interface FlatDaoCustom {
	List<FlatEntity> findNotSoldFlatsBySizeCriteria(FlatSizeSearchCriteria searchCriteriaFrom,
			FlatSizeSearchCriteria searchCriteriaTo);

	Long findFlatCountOfGivenStatusInACertainBuilding(FlatStatus flatStatus, Long buildingId);

	List<FlatEntity> findFlatsSuitedForHandicapped();
}
