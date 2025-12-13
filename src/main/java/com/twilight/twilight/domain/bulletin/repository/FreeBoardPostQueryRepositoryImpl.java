package com.twilight.twilight.domain.bulletin.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twilight.twilight.domain.bulletin.dto.Cursor;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostReplyDto;
import com.twilight.twilight.domain.bulletin.entity.QFreeBoardPost;
import com.twilight.twilight.domain.bulletin.entity.QFreeBoardPostReply;
import com.twilight.twilight.domain.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.twilight.twilight.domain.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class FreeBoardPostQueryRepositoryImpl implements FreeBoardPostQueryRepository{

    private final JPAQueryFactory query;
    QFreeBoardPost qFreeBoardPost = QFreeBoardPost.freeBoardPost;
    QMember qMember = QMember.member;
    QFreeBoardPostReply qFreeBoardPostReply = QFreeBoardPostReply.freeBoardPostReply;
    QFreeBoardPostReply parent = new QFreeBoardPostReply("parent");


    @Override
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

    @Override
    public List<GetFreeBoardPostReplyDto> findTopNParentRepliesOrderByCreatedAtDesc(Long postId, int count) {
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
                .where(
                        qFreeBoardPostReply.freeBoardPost.freeBoardPostId.eq(postId),
                        qFreeBoardPostReply.parentReply.isNull()
                        )
                .join(qFreeBoardPostReply.member,member)
                .leftJoin(qFreeBoardPostReply.parentReply, parent)
                .orderBy(qFreeBoardPostReply.createdAt.desc())
                .limit(count)
                .fetch();
    }

    @Override
    public List<GetFreeBoardPostReplyDto> findParentRepliesOrderByCreatedAtAsc(
            Long postId,
            Long page,
            int size
    ) {
        long offset = (long) (page - 1) * size;

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
                .where(
                        qFreeBoardPostReply.freeBoardPost.freeBoardPostId.eq(postId),
                        qFreeBoardPostReply.parentReply.isNull()
                )
                .join(qFreeBoardPostReply.member, member)
                .leftJoin(qFreeBoardPostReply.parentReply, parent)
                .orderBy(qFreeBoardPostReply.createdAt.asc())
                .offset(offset)
                .limit(size)
                .fetch();
    }

    @Override
    public List<GetFreeBoardPostReplyDto> findLatestChildReplyByReplyId(Long replyId, int count) {
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
                .where(
                        qFreeBoardPostReply.parentReply.freeBoardPostReplyId.eq(replyId)
                )
                .join(qFreeBoardPostReply.member,member)
                .join(qFreeBoardPostReply.parentReply,parent)
                .orderBy(qFreeBoardPostReply.createdAt.desc(),
                        qFreeBoardPostReply.freeBoardPostReplyId.desc()
                        )
                .limit(count)
                .fetch();
    }

    @Override
    public List<GetFreeBoardPostReplyDto> findAllChildReplyByReplyId(Long replyId) {
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
                .where(
                        qFreeBoardPostReply.parentReply.freeBoardPostReplyId.eq(replyId)
                )
                .join(qFreeBoardPostReply.member,member)
                .join(qFreeBoardPostReply.parentReply,parent)
                .orderBy(qFreeBoardPostReply.createdAt.desc(), //최신 -> 과거
                        qFreeBoardPostReply.freeBoardPostReplyId.desc()
                )
                .fetch();
    }

    @Override
    public List<GetFreeBoardPostReplyDto> findChildrenByParentIds(List<Long> parentIds) {
        if (parentIds == null || parentIds.isEmpty()) {
            return List.of();
        }
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
                .join(qFreeBoardPostReply.member, qMember)
                .join(qFreeBoardPostReply.parentReply, parent)
                .where(parent.freeBoardPostReplyId.in(parentIds))
                .orderBy(
                        parent.freeBoardPostReplyId.asc(),              // 부모별로 묶이게
                        qFreeBoardPostReply.createdAt.desc(),           //최신 -> 과거
                        qFreeBoardPostReply.freeBoardPostReplyId.desc()
                )
                .fetch();
    }

    @Override
    public long countParentRepliesByPostId(Long postId) {
        if (postId == null) {
            return 0L;
        }

        Long count = query
                .select(qFreeBoardPostReply.count())
                .from(qFreeBoardPostReply)
                .where(
                        qFreeBoardPostReply.freeBoardPost.freeBoardPostId.eq(postId),
                        qFreeBoardPostReply.parentReply.isNull()
                )
                .fetchOne();

        return (count != null) ? count : 0L;
    }

    @Override
    public List<GetFreeBoardPostListDto> findPostsByCursor(
            Cursor cursor,
            int size) {
        BooleanBuilder where = new BooleanBuilder();

        Long lastId = null;
        LocalDateTime lastCreatedAt = null;

        if (cursor != null) {
            lastId = cursor.lastId();
            lastCreatedAt = cursor.lastCreatedAt();
        }

        if (lastId != null && lastCreatedAt != null) {
            where.and(
                    qFreeBoardPost.createdAt.lt(lastCreatedAt)
                            .or(
                                    qFreeBoardPost.createdAt.eq(lastCreatedAt)
                                            .and(qFreeBoardPost.freeBoardPostId.lt(lastId))

                            )
            );
        }

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
                .where(where)
                .orderBy(
                        qFreeBoardPost.createdAt.desc(),
                        qFreeBoardPost.freeBoardPostId.desc()
                )
                .limit(size + 1)
                .fetch();
    }

}
