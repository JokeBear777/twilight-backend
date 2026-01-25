package com.twilight.twilight.domain.book.entity.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberQuestionAnswer is a Querydsl query type for MemberQuestionAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberQuestionAnswer extends EntityPathBase<MemberQuestionAnswer> {

    private static final long serialVersionUID = 789350755L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberQuestionAnswer memberQuestionAnswer = new QMemberQuestionAnswer("memberQuestionAnswer");

    public final StringPath answer = createString("answer");

    public final QMemberQuestion memberQuestion;

    public final NumberPath<Long> memberQuestionAnswerId = createNumber("memberQuestionAnswerId", Long.class);

    public QMemberQuestionAnswer(String variable) {
        this(MemberQuestionAnswer.class, forVariable(variable), INITS);
    }

    public QMemberQuestionAnswer(Path<? extends MemberQuestionAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberQuestionAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberQuestionAnswer(PathMetadata metadata, PathInits inits) {
        this(MemberQuestionAnswer.class, metadata, inits);
    }

    public QMemberQuestionAnswer(Class<? extends MemberQuestionAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memberQuestion = inits.isInitialized("memberQuestion") ? new QMemberQuestion(forProperty("memberQuestion"), inits.get("memberQuestion")) : null;
    }

}

