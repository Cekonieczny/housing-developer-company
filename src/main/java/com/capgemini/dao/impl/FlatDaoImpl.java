package com.capgemini.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.capgemini.dao.custom.FlatDaoCustom;
import com.capgemini.domain.FlatEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.domain.QBuildingEntity;
import com.capgemini.domain.QFlatEntity;
import com.capgemini.searchcriteria.FlatSizeSearchCriteria;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class FlatDaoImpl implements FlatDaoCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public List<FlatEntity> findNotSoldFlatsBySizeCriteria(FlatSizeSearchCriteria searchCriteriaFrom,
			FlatSizeSearchCriteria searchCriteriaTo) {
		JPAQuery<FlatEntity> q = new JPAQuery<>(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;

		q.from(qflat).select(qflat).where(qflat.flatStatus.ne(FlatStatus.SOLD));
		if (searchCriteriaFrom.getFloorArea() != null && searchCriteriaTo.getFloorArea() != null) {
			q.where(qflat.floorArea.between(searchCriteriaFrom.getFloorArea(), searchCriteriaTo.getFloorArea()));
		}
		if (searchCriteriaFrom.getNumberOfBalconies() != null && searchCriteriaTo.getNumberOfBalconies() != null) {
			q.where(qflat.numberOfBalconies.between(searchCriteriaFrom.getNumberOfBalconies(),
					searchCriteriaTo.getNumberOfBalconies()));
		}
		if (searchCriteriaFrom.getNumberOfRooms() != null && searchCriteriaTo.getNumberOfRooms() != null) {
			q.where(qflat.numberOfRooms.between(searchCriteriaFrom.getNumberOfRooms(),
					searchCriteriaTo.getNumberOfRooms()));
		}
		return q.fetch();
	}

	@Override
	public Long findFlatCountOfGivenStatusInACertainBuilding(FlatStatus flatStatus, Long buildingId) {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
		return qf.select(qflat.count()).from(qbuilding).join(qbuilding.flatEntities, qflat)
				.on(qbuilding.id.eq(buildingId)).where(qflat.flatStatus.eq(flatStatus)).fetchOne();
	}

	@Override
	public List<FlatEntity> findFlatsSuitedForHandicapped() {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
		return qf.selectFrom(qflat).join(qflat.buildingEntity, qbuilding)
				.where(qbuilding.hasLift.eq(true).or(qflat.floorNumber.eq("0"))).fetch();
	}

}
