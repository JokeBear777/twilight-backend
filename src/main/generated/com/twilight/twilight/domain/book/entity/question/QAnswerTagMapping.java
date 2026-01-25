package com.twilight.twilight.domain.book.entity.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswerTagMapping is a Querydsl query type for AnswerTagMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswerTagMapping extends EntityPathBase<AnswerTagMapping> {

    private static final long serialVersionUID = 942519703L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswerTagMapping answerTagMapping = new QAnswerTagMapping("answerTagMapping");

    public final NumberPath<Long> answerTagMappingId = createNumber("answerTagMappingId", Long.class);

    public final QMemberQuestionAnswer memberQuestionAnswer;

    public final com.twilight.twilight.domain.book.entity.tag.QTag tag;

    public QAnswerTagMapping(String variable) {
        this(AnswerTagMapping.class, forVariable(variable), INITS);
    }

    public QAnswerTagMapping(Path<? extends AnswerTagMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswerTagMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswerTagMapping(PathMetadata metadata, PathInits inits) {
        this(AnswerTagMapping.class, metadata, inits);
    }

    public QAnswerTagMapping(Class<? extends AnswerTagMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberQuestionAnswer = inits.isInitialized("memberQuestionAnswer") ? new QMemberQuestionAnswer(forProperty("memberQuestionAnswer"), inits.get("memberQuestionAnswer")) : null;
        this.tag = inits.isInitialized("tag") ? new com.twilight.twilight.domain.book.entity.tag.QTag(forProperty("tag")) : null;
    }

}

