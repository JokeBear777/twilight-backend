package com.twilight.twilight.domain.bulletin.post.dto;

import java.time.LocalDateTime;

public record PageCursorRequest(
        Long lastId,
        LocalDateTime lastCreatedAt,
        Integer size
) {
    public PostCursor toCursor() {
        if (lastId == null || lastCreatedAt == null) {
            return null;
        }
        return new PostCursor(lastId, lastCreatedAt);
    }

    public int pageSizeOrDefault() {
        if (size == null || size <= 0 || size > 100) {
            return 10;
        }
        return size;
    }

    public static PageCursorRequest first(Integer size) {
        return new PageCursorRequest(null, null, size);
    }
}
