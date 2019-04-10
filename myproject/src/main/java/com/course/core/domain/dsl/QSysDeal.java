package com.course.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.course.core.domain.SysDeal;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;

@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysDeal extends EntityPathBase<SysDeal>{
	private static final long serialVersionUID = 833566591L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRole sysdeal = new QUserRole("sysdeal");

    public final QSysDealGroups sysdealgroups;

    public QSysDeal(String variable) {
        this(SysDeal.class, forVariable(variable), INITS);
    }

    public QSysDeal(Path<? extends SysDeal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysDeal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysDeal(PathMetadata metadata, PathInits inits) {
        this(SysDeal.class, metadata, inits);
    }

    public QSysDeal(Class<? extends SysDeal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sysdealgroups = inits.isInitialized("sysdealgroups") ? new QSysDealGroups(forProperty("sysdealgroups"), inits.get("sysdealgroups")) : null;
    }

}
