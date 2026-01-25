package com.twilight.twilight.domain.bulletin.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFreeBoardPostReply is a Querydsl query type for FreeBoardPostReply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFreeBoardPostReply extends EntityPathBase<FreeBoardPostReply> {

    private static final long serialVersionUID = -1857317321L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFreeBoardPostReply freeBoardPostReply = new QFreeBoardPostReply("freeBoardPostReply");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QFreeBoardPost freeBoardPost;

    public final NumberPath<Long> freeBoardPostReplyId = createNumber("freeBoardPostReplyId", Long.class);

    public final com.twilight.twilight.domain.member.entity.QMember member;

    public final QFreeBoardPostReply parentReply;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QFreeBoardPostReply(String variable) {
        this(FreeBoardPostReply.class, forVariable(variable), INITS);
    }

    public QFreeBoardPostReply(Path<? extends FreeBoardPostReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFreeBoardPostReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFreeBoardPostReply(PathMetadata metadata, PathInits inits) {
        this(FreeBoardPostReply.class, metadata, inits);
    }

    public QFreeBoardPostReply(Class<? extends FreeBoardPostReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.freeBoardPost = inits.isInitialized("freeBoardPost") ? new QFreeBoardPost(forProperty("freeBoardPost"), inits.get("freeBoardPost")) : null;
        this.member = inits.isInitialized("member") ? new com.twilight.twilight.domain.member.entity.QMember(forProperty("member")) : null;
        this.parentReply = inits.isInitialized("parentReply") ? new QFreeBoardPostReply(forProperty("parentReply"), inits.get("parentReply")) : null;
    }

}

