package com.twilight.twilight.domain.bulletin.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostReplyDto;
import com.twilight.twilight.domain.bulletin.entity.QFreeBoardPost;
import com.twilight.twilight.domain.bulletin.entity.QFreeBoardPostReply;
import com.twilight.twilight.domain.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twilight.twilight.domain.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class FreeBoardPostQueryRepository {

    private final JPAQueryFactory query;
    QFreeBoardPost qFreeBoardPost = QFreeBoardPost.freeBoardPost;
    QMember qMember = QMember.member;
    QFreeBoardPostReply qFreeBoardPostReply = QFreeBoardPostReply.freeBoardPostReply;
    QFreeBoardPostReply parent = new QFreeBoardPostReply("parent");

    public List<GetFreeBoardPostListDto> findTopNByOrderByCreatedAtDesc(int number) {
        return query
                .select(Projections.constructor(GetFreeBoardPostListDto.class,
                        qFreeBoardPost.freeBoardPostId,
                        qMember.memberId,
                        qFreeBoardPost.title,
                        qFreeBoardPost.content,
                        qMember.memberName,
                        qFreeBoardPost.views,
                        qFreeBoardPost.numberOfRecommendations,
                        qFreeBoardPost.numberOfComments,
                        qFreeBoardPost.createdAt
                ))
                .from(qFreeBoardPost)
                .join(qFreeBoardPost.member,member)
                .orderBy(qFreeBoardPost.createdAt.desc())
                .limit(number)
                .fetch();
    }

    public List<GetFreeBoardPostReplyDto> findTopNRepliesOrderByCreatedAtDesc(Long postId, int count) {
        return query
                .select(Projections.constructor(GetFreeBoardPostReplyDto.class,
                        qFreeBoardPostReply.freeBoardPostReplyId,
                        parent.freeBoardPostReplyId,
                        qMember.memberName,
                        qMember.memberId,
                        qFreeBoardPostReply.content,
                        qFreeBoardPostReply.createdAt,
                        qFreeBoardPostReply.updatedAt
                        ))
                .from(qFreeBoardPostReply)
                .where(qFreeBoardPostReply.freeBoardPost.freeBoardPostId.eq(postId))
                .join(qFreeBoardPostReply.member,member)
                .leftJoin(qFreeBoardPostReply.parentReply, parent)
                .orderBy(qFreeBoardPostReply.createdAt.desc())
                .limit(count)
                .fetch();
    }

}
