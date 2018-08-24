package com.capgemini.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderEntity is a Querydsl query type for OrderEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrderEntity extends EntityPathBase<OrderEntity> {

    private static final long serialVersionUID = -515199845L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderEntity orderEntity = new QOrderEntity("orderEntity");

    public final QFlatEntity flatOrdered;

    public final QCustomerEntity owner;

    public final SetPath<CustomerEntity, QCustomerEntity> subOwners = this.<CustomerEntity, QCustomerEntity>createSet("subOwners", CustomerEntity.class, QCustomerEntity.class, PathInits.DIRECT2);

    public QOrderEntity(String variable) {
        this(OrderEntity.class, forVariable(variable), INITS);
    }

    public QOrderEntity(Path<? extends OrderEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderEntity(PathMetadata metadata, PathInits inits) {
        this(OrderEntity.class, metadata, inits);
    }

    public QOrderEntity(Class<? extends OrderEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.flatOrdered = inits.isInitialized("flatOrdered") ? new QFlatEntity(forProperty("flatOrdered"), inits.get("flatOrdered")) : null;
        this.owner = inits.isInitialized("owner") ? new QCustomerEntity(forProperty("owner"), inits.get("owner")) : null;
    }

}

