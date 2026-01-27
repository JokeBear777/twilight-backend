package com.twilight.twilight.domain.member.follow.dto;

public record FollowCursorRequest(
        Long lastId,
        Integer size
) {
    public FollowCursor toCursor() {
        if (lastId == null) {
            return null;
        }
        return new FollowCursor(lastId);
    }


    public int pageSizeOrDefault() {
        if (size == null || size <= 0 || size > 100) {
            return 10;
        }
        return size;
    }

    public static FollowCursorRequest first(Integer size) {
        return new FollowCursorRequest(null ,size);
    }
}
