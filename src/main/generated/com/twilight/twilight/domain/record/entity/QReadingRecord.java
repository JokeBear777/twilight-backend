package com.twilight.twilight.domain.record.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReadingRecord is a Querydsl query type for ReadingRecord
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReadingRecord extends EntityPathBase<ReadingRecord> {

    private static final long serialVersionUID = -968343950L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReadingRecord readingRecord = new QReadingRecord("readingRecord");

    public final QBookRecord bookRecord;

    public final StringPath contents = createString("contents");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> readingRecordId = createNumber("readingRecordId", Long.class);

    public QReadingRecord(String variable) {
        this(ReadingRecord.class, forVariable(variable), INITS);
    }

    public QReadingRecord(Path<? extends ReadingRecord> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReadingRecord(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReadingRecord(PathMetadata metadata, PathInits inits) {
        this(ReadingRecord.class, metadata, inits);
    }

    public QReadingRecord(Class<? extends ReadingRecord> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bookRecord = inits.isInitialized("bookRecord") ? new QBookRecord(forProperty("bookRecord"), inits.get("bookRecord")) : null;
    }

}

