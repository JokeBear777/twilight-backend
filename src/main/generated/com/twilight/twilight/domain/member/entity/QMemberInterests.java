package com.twilight.twilight.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberInterests is a Querydsl query type for MemberInterests
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberInterests extends EntityPathBase<MemberInterests> {

    private static final long serialVersionUID = -1306990629L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberInterests memberInterests = new QMemberInterests("memberInterests");

    public final QInterest interest;

    public final QMember member;

    public final NumberPath<Long> memberInterestsId = createNumber("memberInterestsId", Long.class);

    public QMemberInterests(String variable) {
        this(MemberInterests.class, forVariable(variable), INITS);
    }

    public QMemberInterests(Path<? extends MemberInterests> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberInterests(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberInterests(PathMetadata metadata, PathInits inits) {
        this(MemberInterests.class, metadata, inits);
    }

    public QMemberInterests(Class<? extends MemberInterests> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interest = inits.isInitialized("interest") ? new QInterest(forProperty("interest")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

