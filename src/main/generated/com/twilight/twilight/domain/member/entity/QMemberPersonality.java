package com.twilight.twilight.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberPersonality is a Querydsl query type for MemberPersonality
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberPersonality extends EntityPathBase<MemberPersonality> {

    private static final long serialVersionUID = 10053440L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberPersonality memberPersonality = new QMemberPersonality("memberPersonality");

    public final QMember member;

    public final NumberPath<Long> memberPersonalityId = createNumber("memberPersonalityId", Long.class);

    public final QPersonality personality;

    public QMemberPersonality(String variable) {
        this(MemberPersonality.class, forVariable(variable), INITS);
    }

    public QMemberPersonality(Path<? extends MemberPersonality> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberPersonality(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberPersonality(PathMetadata metadata, PathInits inits) {
        this(MemberPersonality.class, metadata, inits);
    }

    public QMemberPersonality(Class<? extends MemberPersonality> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.personality = inits.isInitialized("personality") ? new QPersonality(forProperty("personality")) : null;
    }

}

