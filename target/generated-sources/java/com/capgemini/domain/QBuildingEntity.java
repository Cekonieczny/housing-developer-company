package com.capgemini.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuildingEntity is a Querydsl query type for BuildingEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBuildingEntity extends EntityPathBase<BuildingEntity> {

    private static final long serialVersionUID = -1928202323L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuildingEntity buildingEntity = new QBuildingEntity("buildingEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    //inherited
    public final DateTimePath<java.util.Date> createdOn = _super.createdOn;

    public final StringPath description = createString("description");

    public final SetPath<FlatEntity, QFlatEntity> flatEntities = this.<FlatEntity, QFlatEntity>createSet("flatEntities", FlatEntity.class, QFlatEntity.class, PathInits.DIRECT2);

    public final BooleanPath hasLift = createBoolean("hasLift");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.capgemini.embeddable.QLocation location;

    public final NumberPath<Integer> numberOfFlats = createNumber("numberOfFlats", Integer.class);

    public final NumberPath<Integer> numberOfFloors = createNumber("numberOfFloors", Integer.class);

    //inherited
    public final DateTimePath<java.util.Date> updatedOn = _super.updatedOn;

    public QBuildingEntity(String variable) {
        this(BuildingEntity.class, forVariable(variable), INITS);
    }

    public QBuildingEntity(Path<? extends BuildingEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuildingEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuildingEntity(PathMetadata metadata, PathInits inits) {
        this(BuildingEntity.class, metadata, inits);
    }

    public QBuildingEntity(Class<? extends BuildingEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new com.capgemini.embeddable.QLocation(forProperty("location")) : null;
    }

}

