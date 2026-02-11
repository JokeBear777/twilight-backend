package com.twilight.twilight.domain.bulletin.feed.stat;

import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class AuthorViewStat {

    private final Long viewerId;
    private final Long authorId;
    private final int score;
    private final LocalDateTime lastViewedAt;

    public boolean isStale(Duration window) {
        return lastViewedAt.isBefore(LocalDateTime.now().minus(window));
    }
}
