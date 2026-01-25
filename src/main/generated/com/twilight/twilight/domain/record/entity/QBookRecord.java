package com.twilight.twilight.domain.record.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookRecord is a Querydsl query type for BookRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookRecord extends EntityPathBase<BookRecord> {

    private static final long serialVersionUID = -1862620091L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookRecord bookRecord = new QBookRecord("bookRecord");

    public final com.twilight.twilight.domain.book.entity.book.QBook book;

    public final NumberPath<Long> bookRecordId = createNumber("bookRecordId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final com.twilight.twilight.domain.member.entity.QMember member;

    public QBookRecord(String variable) {
        this(BookRecord.class, forVariable(variable), INITS);
    }

    public QBookRecord(Path<? extends BookRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookRecord(PathMetadata metadata, PathInits inits) {
        this(BookRecord.class, metadata, inits);
    }

    public QBookRecord(Class<? extends BookRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.twilight.twilight.domain.book.entity.book.QBook(forProperty("book")) : null;
        this.member = inits.isInitialized("member") ? new com.twilight.twilight.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

