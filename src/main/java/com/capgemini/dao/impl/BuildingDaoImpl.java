package com.capgemini.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.capgemini.dao.custom.BuildingDaoCustom;
import com.capgemini.domain.BuildingEntity;
import com.capgemini.domain.FlatStatus;
import com.capgemini.domain.QBuildingEntity;
import com.capgemini.domain.QFlatEntity;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class BuildingDaoImpl implements BuildingDaoCustom {

	@PersistenceContext
	EntityManager em;

	@Override
	public Double findAvgFlatPriceInACertainBuilding(Long buildingId) {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
		return qf.select(qflat.price.avg()).from(qbuilding).join(qbuilding.flatEntities, qflat)
				.on(qbuilding.id.eq(buildingId)).fetchOne();
	}

	@Override
	public List<BuildingEntity> findBuildingWithMostQuantityOfFlatsFree() {
		JPAQueryFactory qf = new JPAQueryFactory(em);
		QFlatEntity qflat = QFlatEntity.flatEntity;
		QBuildingEntity qbuilding = QBuildingEntity.buildingEntity;
		JPAQuery<Long> q1 = new JPAQuery<>(em);

		Long maxCount = q1.select(qflat.count()).from(qbuilding).join(qbuilding.flatEntities, qflat)
				.where(qflat.flatStatus.eq(FlatStatus.FREE)).groupBy(qbuilding).orderBy(qflat.count().desc())
				.fetchFirst();

		return qf.select(qbuilding).from(qbuilding)
				.where(qbuilding.in(JPAExpressions.select(qbuilding).from(qflat).join(qflat.buildingEntity, qbuilding)
						.on(qflat.flatStatus.eq(FlatStatus.FREE)).groupBy(qbuilding)
						.having(qflat.count().eq(maxCount))))
				.fetch();
	}

}
