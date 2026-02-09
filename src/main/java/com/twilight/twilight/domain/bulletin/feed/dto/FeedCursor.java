package com.twilight.twilight.domain.bulletin.feed.dto;

import com.twilight.twilight.global.cursor.Cursor;
import lombok.Getter;

import java.time.LocalDateTime;

public record FeedCursor(
        Long lastFeedEventId,
        LocalDateTime lastCreatedAt
) implements Cursor {

    public boolean isFirst() {
        return lastFeedEventId == null || lastCreatedAt == null;
    }
}
