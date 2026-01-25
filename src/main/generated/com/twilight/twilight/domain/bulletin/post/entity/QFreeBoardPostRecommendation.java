package com.twilight.twilight.domain.bulletin.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoardPostRecommendation is a Querydsl query type for FreeBoardPostRecommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeBoardPostRecommendation extends EntityPathBase<FreeBoardPostRecommendation> {

    private static final long serialVersionUID = -1502272756L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeBoardPostRecommendation freeBoardPostRecommendation = new QFreeBoardPostRecommendation("freeBoardPostRecommendation");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.twilight.twilight.domain.member.entity.QMember member;

    public final QFreeBoardPost post;

    public QFreeBoardPostRecommendation(String variable) {
        this(FreeBoardPostRecommendation.class, forVariable(variable), INITS);
    }

    public QFreeBoardPostRecommendation(Path<? extends FreeBoardPostRecommendation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeBoardPostRecommendation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeBoardPostRecommendation(PathMetadata metadata, PathInits inits) {
        this(FreeBoardPostRecommendation.class, metadata, inits);
    }

    public QFreeBoardPostRecommendation(Class<? extends FreeBoardPostRecommendation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.twilight.twilight.domain.member.entity.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new QFreeBoardPost(forProperty("post"), inits.get("post")) : null;
    }

}

