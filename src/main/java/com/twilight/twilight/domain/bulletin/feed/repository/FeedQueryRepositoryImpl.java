package com.twilight.twilight.domain.bulletin.feed.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursor;
import com.twilight.twilight.domain.bulletin.feed.dto.FeedCursorRequest;
import com.twilight.twilight.domain.bulletin.feed.entity.QFeedEvent;
import com.twilight.twilight.domain.bulletin.feed.heuristic.FeedHeuristicCandidate;
import com.twilight.twilight.domain.member.follow.entity.QFollow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.twilight.twilight.domain.bulletin.feed.entity.QFeedEvent.feedEvent;
import static com.twilight.twilight.domain.member.follow.entity.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FeedQueryRepositoryImpl implements FeedQueryRepository {

    private final JPAQueryFactory query;
    QFeedEvent qFeedEvent = feedEvent;
    QFollow qFollow = follow;

    @Override
    public List<FeedHeuristicCandidate> findHeuristicCandidatesByCursor(
            FeedCursor cursor,
            Long memberId,
            int pageSize
    ) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (cursor != null
                && cursor.lastFeedEventId() != null
                && cursor.lastCreatedAt() != null) {

            whereClause.and(
                    feedEvent.createdAt.lt(cursor.lastCreatedAt())
                            .or(
                                    feedEvent.createdAt.eq(cursor.lastCreatedAt())
                                            .and(feedEvent.id.lt(cursor.lastFeedEventId()))
                            )
            );
        }

        // 팔로우 조건 (한 번에)
        QFollow follow = QFollow.follow;

        whereClause.and(
                JPAExpressions
                        .selectOne()
                        .from(follow)
                        .where(
                                follow.follower.memberId.eq(memberId),
                                follow.following.memberId.eq(feedEvent.actorId)
                        )
                        .exists()
        );

        return query
                .select(
                        Projections.constructor(
                                FeedHeuristicCandidate.class,
                                feedEvent.id,
                                feedEvent.eventType,
                                feedEvent.targetId,
                                feedEvent.actorId,
                                feedEvent.createdAt
                        )
                )
                .from(feedEvent)
                .where(whereClause)
                .orderBy(
                        feedEvent.createdAt.desc(),
                        feedEvent.id.desc()
                )
                .limit(pageSize)
                .fetch();
    }

}
