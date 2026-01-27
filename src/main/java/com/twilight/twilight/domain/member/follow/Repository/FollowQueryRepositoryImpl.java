package com.twilight.twilight.domain.member.follow.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twilight.twilight.domain.member.follow.dto.FollowCursor;
import com.twilight.twilight.domain.member.follow.dto.GetFollowListDto;
import com.twilight.twilight.domain.member.follow.entity.QFollow;
import com.twilight.twilight.domain.member.member.entity.QMember;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.twilight.twilight.domain.member.follow.entity.QFollow.follow;
import static com.twilight.twilight.domain.member.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepositoryImpl implements FollowQueryRepository {


    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    QMember qMember = member;
    QFollow follow = QFollow.follow;

    @Override
    public void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId) {
        queryFactory
                .delete(follow)
                .where(
                        follow.follower.memberId.eq(followerId),
                        follow.following.memberId.eq(followingId)
                )
                .execute();
        em.clear();
    }

    @Override
    public List<GetFollowListDto> findFollowerListByCursor(
            FollowCursor cursor,
            int pageSize,
            Long targetMemberId) {
        BooleanBuilder where = new BooleanBuilder();

        where.and(follow.following.memberId.eq(targetMemberId));

        if (cursor != null && cursor.lastId() != null) {
            where.and(follow.followId.lt(cursor.lastId()));
        }

        return queryFactory
                .select(Projections.constructor(
                        GetFollowListDto.class,
                        member.memberId,
                        member.memberName,
                        Expressions.constant("https://mond-al.github.io/assets/images/forTest/ratio/all_ratio/image_3_320x240.png") // selfImageUrl mock
                ))
                .from(follow)
                .join(follow.follower, member)
                .where(where)
                .orderBy(follow.followId.desc())
                .limit(pageSize)
                .fetch();
    }
}
