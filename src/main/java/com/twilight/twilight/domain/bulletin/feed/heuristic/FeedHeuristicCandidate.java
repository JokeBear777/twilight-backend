package com.twilight.twilight.domain.bulletin.feed.heuristic;

import com.twilight.twilight.domain.bulletin.feed.type.FeedEventType;

import java.time.LocalDateTime;

public record FeedHeuristicCandidate(
        Long feedEventId,
        FeedEventType eventType,
        Long targetId,
        Long actorId,
        LocalDateTime createdAt
) {}
