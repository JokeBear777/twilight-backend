package com.twilight.twilight.domain.bulletin.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoardPost is a Querydsl query type for FreeBoardPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeBoardPost extends EntityPathBase<FreeBoardPost> {

    private static final long serialVersionUID = 742284403L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeBoardPost freeBoardPost = new QFreeBoardPost("freeBoardPost");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> freeBoardPostId = createNumber("freeBoardPostId", Long.class);

    public final com.twilight.twilight.domain.member.entity.QMember member;

    public final NumberPath<Integer> numberOfComments = createNumber("numberOfComments", Integer.class);

    public final NumberPath<Integer> numberOfRecommendations = createNumber("numberOfRecommendations", Integer.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public QFreeBoardPost(String variable) {
        this(FreeBoardPost.class, forVariable(variable), INITS);
    }

    public QFreeBoardPost(Path<? extends FreeBoardPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeBoardPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeBoardPost(PathMetadata metadata, PathInits inits) {
        this(FreeBoardPost.class, metadata, inits);
    }

    public QFreeBoardPost(Class<? extends FreeBoardPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.twilight.twilight.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

