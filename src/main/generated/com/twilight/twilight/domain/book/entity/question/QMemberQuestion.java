package com.twilight.twilight.domain.book.entity.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberQuestion is a Querydsl query type for MemberQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberQuestion extends EntityPathBase<MemberQuestion> {

    private static final long serialVersionUID = -864082619L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberQuestion memberQuestion = new QMemberQuestion("memberQuestion");

    public final NumberPath<Long> memberQuestionId = createNumber("memberQuestionId", Long.class);

    public final StringPath question = createString("question");

    public final EnumPath<MemberQuestion.questionType> questionType = createEnum("questionType", MemberQuestion.questionType.class);

    public final com.twilight.twilight.domain.book.entity.tag.QTag tag;

    public QMemberQuestion(String variable) {
        this(MemberQuestion.class, forVariable(variable), INITS);
    }

    public QMemberQuestion(Path<? extends MemberQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberQuestion(PathMetadata metadata, PathInits inits) {
        this(MemberQuestion.class, metadata, inits);
    }

    public QMemberQuestion(Class<? extends MemberQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tag = inits.isInitialized("tag") ? new com.twilight.twilight.domain.book.entity.tag.QTag(forProperty("tag")) : null;
    }

}

