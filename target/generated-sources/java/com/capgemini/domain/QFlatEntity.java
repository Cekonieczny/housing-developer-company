package com.capgemini.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFlatEntity is a Querydsl query type for FlatEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QFlatEntity extends EntityPathBase<FlatEntity> {

    private static final long serialVersionUID = 1973610418L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFlatEntity flatEntity = new QFlatEntity("flatEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final QBuildingEntity buildingEntity;

    //inherited
    public final DateTimePath<java.util.Date> createdOn = _super.createdOn;

    public final SetPath<CustomerEntity, QCustomerEntity> customerEntities = this.<CustomerEntity, QCustomerEntity>createSet("customerEntities", CustomerEntity.class, QCustomerEntity.class, PathInits.DIRECT2);

    public final StringPath flatNumber = createString("flatNumber");

    public final EnumPath<FlatStatus> flatStatus = createEnum("flatStatus", FlatStatus.class);

    public final NumberPath<Integer> floorArea = createNumber("floorArea", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final NumberPath<Integer> numberOfBalconies = createNumber("numberOfBalconies", Integer.class);

    public final NumberPath<Integer> numberOfRooms = createNumber("numberOfRooms", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updatedOn = _super.updatedOn;

    public QFlatEntity(String variable) {
        this(FlatEntity.class, forVariable(variable), INITS);
    }

    public QFlatEntity(Path<? extends FlatEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFlatEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFlatEntity(PathMetadata metadata, PathInits inits) {
        this(FlatEntity.class, metadata, inits);
    }

    public QFlatEntity(Class<? extends FlatEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buildingEntity = inits.isInitialized("buildingEntity") ? new QBuildingEntity(forProperty("buildingEntity"), inits.get("buildingEntity")) : null;
    }

}

