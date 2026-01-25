package com.twilight.twilight.domain.book.entity.book;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookTags is a Querydsl query type for BookTags
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookTags extends EntityPathBase<BookTags> {

    private static final long serialVersionUID = -234654236L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookTags bookTags = new QBookTags("bookTags");

    public final QBook book;

    public final NumberPath<Long> bookTagsId = createNumber("bookTagsId", Long.class);

    public final com.twilight.twilight.domain.book.entity.tag.QTag tag;

    public QBookTags(String variable) {
        this(BookTags.class, forVariable(variable), INITS);
    }

    public QBookTags(Path<? extends BookTags> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookTags(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookTags(PathMetadata metadata, PathInits inits) {
        this(BookTags.class, metadata, inits);
    }

    public QBookTags(Class<? extends BookTags> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
        this.tag = inits.isInitialized("tag") ? new com.twilight.twilight.domain.book.entity.tag.QTag(forProperty("tag")) : null;
    }

}

