package com.twilight.twilight.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPersonality is a Querydsl query type for Personality
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonality extends EntityPathBase<Personality> {

    private static final long serialVersionUID = -1267673286L;

    public static final QPersonality personality = new QPersonality("personality");

    public final StringPath name = createString("name");

    public final NumberPath<Long> personalityId = createNumber("personalityId", Long.class);

    public QPersonality(String variable) {
        super(Personality.class, forVariable(variable));
    }

    public QPersonality(Path<? extends Personality> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPersonality(PathMetadata metadata) {
        super(Personality.class, metadata);
    }

}

