package com.twilight.twilight.domain.bulletin.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twilight.twilight.domain.bulletin.dto.GetFreeBoardPostListDto;
import com.twilight.twilight.domain.bulletin.entity.QFreeBoardPost;
import com.twilight.twilight.domain.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twilight.twilight.domain.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class FreeBoardPostQueryRepository {

    private final JPAQueryFactory query;

    public List<GetFreeBoardPostListDto> findTopNByOrderByCreatedAtDesc(int number) {
        QFreeBoardPost qFreeBoardPost = QFreeBoardPost.freeBoardPost;
        QMember qMember = QMember.member;

        return query
                .select(Projections.constructor(GetFreeBoardPostListDto.class,
                        qFreeBoardPost.freeBoardPostId,
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

}
