package com.twilight.twilight.domain.book.entity.recommendation;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecommendation is a Querydsl query type for Recommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecommendation extends EntityPathBase<Recommendation> {

    private static final long serialVersionUID = -2011423125L;

    public static final QRecommendation recommendation = new QRecommendation("recommendation");

    public final StringPath aiAnswer = createString("aiAnswer");

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public QRecommendation(String variable) {
        super(Recommendation.class, forVariable(variable));
    }

    public QRecommendation(Path<? extends Recommendation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecommendation(PathMetadata metadata) {
        super(Recommendation.class, metadata);
    }

}

