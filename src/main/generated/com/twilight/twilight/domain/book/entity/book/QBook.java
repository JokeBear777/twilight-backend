package com.twilight.twilight.domain.book.entity.book;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -157066453L;

    public static final QBook book = new QBook("book");

    public final StringPath author = createString("author");

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    public final StringPath category = createString("category");

    public final StringPath coverImageUrl = createString("coverImageUrl");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> pageCount = createNumber("pageCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> publishedAt = createDateTime("publishedAt", java.time.LocalDateTime.class);

    public final StringPath publisher = createString("publisher");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

