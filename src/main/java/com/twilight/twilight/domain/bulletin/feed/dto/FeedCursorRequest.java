package com.twilight.twilight.domain.bulletin.feed.dto;

import com.twilight.twilight.domain.bulletin.post.dto.PageCursorRequest;

import java.time.LocalDateTime;

public record FeedCursorRequest (
        Long lastId,
        LocalDateTime lastCreatedAt,
        Integer size
){

    public FeedCursor toCursor() {
        if (lastId == null || lastCreatedAt == null) {
            return null;
        }

        return new FeedCursor(lastId, lastCreatedAt);
    }

    public int pageSizeOrDefault() {
        if (size == null || size <= 0 || size > 100) {
            return 10;
        }
        return size;
    }

    public static FeedCursorRequest first(Integer size) {
        return new FeedCursorRequest(null, null, size);
    }

}
