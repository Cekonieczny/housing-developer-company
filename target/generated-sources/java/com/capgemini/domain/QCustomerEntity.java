package com.capgemini.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomerEntity is a Querydsl query type for CustomerEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomerEntity extends EntityPathBase<CustomerEntity> {

    private static final long serialVersionUID = -767992489L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomerEntity customerEntity = new QCustomerEntity("customerEntity");

    public final QAbstractEntity _super = new QAbstractEntity(this);

    public final com.capgemini.embeddable.QAddress address;

    public final DateTimePath<java.util.Date> birthDate = createDateTime("birthDate", java.util.Date.class);

    //inherited
    public final DateTimePath<java.util.Date> createdOn = _super.createdOn;

    public final StringPath creditCardNumber = createString("creditCardNumber");

    public final StringPath email = createString("email");

    public final SetPath<FlatEntity, QFlatEntity> flatEntities = this.<FlatEntity, QFlatEntity>createSet("flatEntities", FlatEntity.class, QFlatEntity.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final com.capgemini.embeddable.QName name;

    public final StringPath phoneNumber = createString("phoneNumber");

    //inherited
    public final DateTimePath<java.util.Date> updatedOn = _super.updatedOn;

    public QCustomerEntity(String variable) {
        this(CustomerEntity.class, forVariable(variable), INITS);
    }

    public QCustomerEntity(Path<? extends CustomerEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomerEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomerEntity(PathMetadata metadata, PathInits inits) {
        this(CustomerEntity.class, metadata, inits);
    }

    public QCustomerEntity(Class<? extends CustomerEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new com.capgemini.embeddable.QAddress(forProperty("address"), inits.get("address")) : null;
        this.name = inits.isInitialized("name") ? new com.capgemini.embeddable.QName(forProperty("name")) : null;
    }

}

