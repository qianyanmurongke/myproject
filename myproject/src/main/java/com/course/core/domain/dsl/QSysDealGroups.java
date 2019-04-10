package com.course.core.domain.dsl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import javax.annotation.Generated;

import com.course.core.domain.SysDealGroups;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathInits;

@Generated("com.querydsl.codegen.EntitySerializer")
public class QSysDealGroups extends EntityPathBase<SysDealGroups>{
	private static final long serialVersionUID = -211983203L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSysDealGroups sysdealgroups = new QSysDealGroups("sysdealgroups");
    
    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QOrg org; 
    
    public final QUser creator; 
    
    public final QUser updator; 
 
    public QSysDealGroups(String variable) {
        this(SysDealGroups.class, forVariable(variable), INITS);
    }

    public QSysDealGroups(Path<? extends SysDealGroups> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSysDealGroups(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSysDealGroups(PathMetadata metadata, PathInits inits) {
        this(SysDealGroups.class, metadata, inits);
    }

    public QSysDealGroups(Class<? extends SysDealGroups> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.org = inits.isInitialized("org") ? new QOrg(forProperty("org"), inits.get("org")) : null;
        this.creator = inits.isInitialized("creator") ? new QUser(forProperty("creator"), inits.get("creator")) : null;
        this.updator = inits.isInitialized("updator") ? new QUser(forProperty("updator"), inits.get("updator")) : null;
    }
}
