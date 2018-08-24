package com.capgemini.embeddable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QName is a Querydsl query type for Name
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QName extends BeanPath<Name> {

    private static final long serialVersionUID = -668967712L;

    public static final QName name = new QName("name");

    public final StringPath firstName = createString("firstName");

    public final StringPath lastName = createString("lastName");

    public QName(String variable) {
        super(Name.class, forVariable(variable));
    }

    public QName(Path<? extends Name> path) {
        super(path.getType(), path.getMetadata());
    }

    public QName(PathMetadata metadata) {
        super(Name.class, metadata);
    }

}

