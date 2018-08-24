package com.capgemini.embeddable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QLocation is a Querydsl query type for Location
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QLocation extends BeanPath<Location> {

    private static final long serialVersionUID = -391557238L;

    public static final QLocation location = new QLocation("location");

    public final StringPath buildingNumber = createString("buildingNumber");

    public final StringPath placeName = createString("placeName");

    public final StringPath streetName = createString("streetName");

    public final StringPath zipCode = createString("zipCode");

    public QLocation(String variable) {
        super(Location.class, forVariable(variable));
    }

    public QLocation(Path<? extends Location> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLocation(PathMetadata metadata) {
        super(Location.class, metadata);
    }

}

