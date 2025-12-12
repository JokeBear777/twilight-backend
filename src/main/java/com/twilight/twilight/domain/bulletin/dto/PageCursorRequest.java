package com.twilight.twilight.domain.bulletin.dto;

import java.time.LocalDateTime;

public record PageCursorRequest(
        Long lastId,
        LocalDateTime lastCreatedAt,
        Integer size
) {
    public Cursor toCursor() {
        if (lastId == null || lastCreatedAt == null) {
            return null;
        }
        return new Cursor(lastId, lastCreatedAt);
    }

    public int pageSizeOrDefault() {
        if (size == null || size <= 0 || size > 100) {
            return 10;
        }
        return size;
    }
}
